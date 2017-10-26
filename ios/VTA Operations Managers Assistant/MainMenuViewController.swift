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
    
    var emergencySpreadsheetId = "1qOHnxoBuYycIAfLJ5QBY9vEZvTd3rxub7BivOeriliw"
    var ocpSpreadsheetId = "1xjEZ_pImoxOHSPsLUrVEUs8tUNeBxmq9TfE6CjE0tR8"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
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
    
    func fetchFilePath(name: String) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: name)
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                let filePath = result.value(forKey: "string") as! String
                
            }
        }
        catch {
            //ERROR
        }
    }


}
