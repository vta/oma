import GoogleAPIClientForREST
import GoogleSignIn
import UIKit
import CoreData

class MainMenuViewController: UIViewController, GIDSignInDelegate, GIDSignInUIDelegate {
    
    //MARK: Buttons
    
    @IBOutlet weak var emergencyButton: UIButton!
    @IBOutlet weak var ocpButton: UIButton!
    
    
    // If modifying these scopes, delete your previously saved credentials by
    // resetting the iOS simulator or uninstall the app.
    private let scopes = [kGTLRAuthScopeSheetsSpreadsheetsReadonly]
    
    private let service = GTLRSheetsService()
    let signInButton = GIDSignInButton()
    var rows: [Any] = []
    var listNavigationController: ListNavigationController = ListNavigationController.init()
    
    var emergencySpreadsheetId = ""
    var ocpSpreadsheetId = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fetchMetatData()
        
        // Configure Google Sign-in.
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().scopes = scopes
        GIDSignIn.sharedInstance().signInSilently()
        
        // Add the sign-in button.
        view.addSubview(signInButton)
    }
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!,
              withError error: Error!) {
        if let error = error {
            print("\nerror\n")
            showAlert(title: "Authentication Error", message:
                error.localizedDescription)
            self.service.authorizer = nil
        } else {
            self.signInButton.isHidden = true
            self.service.authorizer = user.authentication.fetcherAuthorizer()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func listRows(spreadsheetId: String, range: String){
        let query = GTLRSheetsQuery_SpreadsheetsValuesGet
            .query(withSpreadsheetId: spreadsheetId, range:range)
        service.executeQuery(query,
                             delegate: self,
                             didFinish: #selector(displayResultWithTicket(ticket:finishedWithObject:error:))
        )
    }
    
    // Process the response and display output
    func displayResultWithTicket(ticket: GTLRServiceTicket,
                                 finishedWithObject result : GTLRSheets_ValueRange,
                                 error : NSError?){
        
        if let error = error {
            showAlert(title: "Error", message: error.localizedDescription)
            return
        }
        if((result.values) != nil && (result.values?.count)! > 0) {
            self.rows = result.values!
            listNavigationController.setRows(rows: self.rows)
        }
    }
    
    // Helper for showing an alert
    func showAlert(title : String, message: String) {
        let alert = UIAlertController(
            title: title,
            message: message,
            preferredStyle: UIAlertControllerStyle.alert
        )
        let ok = UIAlertAction(
            title: "OK",
            style: UIAlertActionStyle.default,
            handler: nil
        )
        alert.addAction(ok)
        present(alert, animated: true, completion: nil)
    }
    
    func writePriority(token: String, newPriority: Int, oldPriority: Int, column: String) {
        let spreadsheetId = "1PN4USOBuPylNzyJ3xxNvw5uEUxoPzSr"
        let range = "Sheet1!" + column + ""
        let valueRange = GTLRSheets_ValueRange.init();
        valueRange.values = [
            [token]
        ]
        let query = GTLRSheetsQuery_SpreadsheetsValuesAppend
            .query(withObject: valueRange, spreadsheetId:spreadsheetId, range:range)
        query.valueInputOption = "USER_ENTERED"
        service.executeQuery(query,
                             delegate: self,
                             didFinish: #selector(displayResultWithTicket(ticket:finishedWithObject:error:)))
    }
    
    func fetchMetatData() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        var request = NSFetchRequest<NSFetchRequestResult>(entityName: "EmergencyPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                emergencySpreadsheetId = result.value(forKey: "string") as! String
                
            }
        }
        catch {
            //ERROR
        }
        
        request = NSFetchRequest<NSFetchRequestResult>(entityName: "OCPPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                ocpSpreadsheetId = result.value(forKey: "string") as! String
            }
        }
        catch {
            //ERROR
        }
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
        var range = ""
        var sheetId = ""
        
        if (sender as! UIButton) == self.emergencyButton {
            range = "Sheet1!A3:Q"
            sheetId = emergencySpreadsheetId
        }
        
        if (sender as! UIButton) == self.ocpButton {
            range = "Sheet1!A3:L"
            sheetId = ocpSpreadsheetId
        }
        
        if segue.destination is ListNavigationController {
            self.listNavigationController = segue.destination as! ListNavigationController
            listRows(spreadsheetId: sheetId, range: range)
        }
    }

}
