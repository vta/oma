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
    private let scopes = [kGTLRAuthScopeSheetsSpreadsheets]
    
    private let service = GTLRSheetsService()
    let signInButton = GIDSignInButton()
    var rows: [Any] = []
    var listNavigationController: ListNavigationController = ListNavigationController.init()
    var oldPriority = -1
    var newPriority = -1
    var priorityColumn = "zz"
    var deviceToken = ""
    
    var emergencySpreadsheetId = ""
    var ocpSpreadsheetId = ""
    var prioritySpreadsheetId = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Configure Google Sign-in.
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().scopes = scopes
        GIDSignIn.sharedInstance().signInSilently()
        
        // Add the sign-in button.
//        signInButton.frame = CGRect.init(x: self.view.frame.size.width / 2, y: self.view.frame.size.height / 2, width: self.view.frame.size.width, height: self.view.frame.size.height)
        view.addSubview(signInButton)
        fetchMetatData()
        if(oldPriority != newPriority) {
            if(newPriority == 0){
                clearPriority()
            } else {
                writePriority()
            }
        }
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
            print(error.localizedDescription)
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
    
    func clearPriority() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "DeviceToken")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                deviceToken = (result.value(forKey: "string") as? String)!
            }
        } catch {
            print("error retrieving device token")
            //ERROR
        }
        
        let query1 = GTLRSheetsQuery_SpreadsheetsValuesGet
            .query(withSpreadsheetId: prioritySpreadsheetId, range:"Sheet1!A:E")
        service.executeQuery(query1,
                             delegate: self,
                             didFinish: #selector(handleAndRemove(ticket:finishedWithObject:error:))
        )
    }
    
    func writePriority() {
        clearPriority()
        
        let valueRange = GTLRSheets_ValueRange.init();
        valueRange.values = [[deviceToken]]
        
        let query = GTLRSheetsQuery_SpreadsheetsValuesAppend
            .query(withObject: valueRange, spreadsheetId:prioritySpreadsheetId, range:"Sheet1!" + priorityColumn + ":" + priorityColumn)
        query.valueInputOption = "USER_ENTERED"
        service.executeQuery(query,
                             delegate: self,
                             didFinish: #selector(handleTicket(ticket:finishedWithObject:error:)))
    }
    
    // Process the response and remove occurrences
    func handleAndRemove(ticket: GTLRServiceTicket,
                      finishedWithObject result : GTLRSheets_ValueRange,
                      error : NSError?){
        
        if let error = error {
            print("error: ")
            print(error.localizedDescription)
        } else {
            if( (result.values) != nil && (result.values?.count)! > 0) {
                var arr = result.values!
                if(arr.count > 0) {
                    for i in 0...arr.count - 1 {
                        if arr[i].count > 0 {
                            for j in 0...arr[i].count - 1  {
                                if(arr[i][j] as! String == deviceToken) {
                                    print("found")
                                    arr[i][j] = ""
                                }
                            }
                        }
                    }
                }
                let valueRange = GTLRSheets_ValueRange.init();
                valueRange.values = arr
                
                let query = GTLRSheetsQuery_SpreadsheetsValuesUpdate
                    .query(withObject: valueRange, spreadsheetId:prioritySpreadsheetId, range:"Sheet1!A:E")
                query.valueInputOption = "USER_ENTERED"
                service.executeQuery(query,
                                     delegate: self,
                                     didFinish: #selector(handleTicket(ticket:finishedWithObject:error:)))
            }
        }
        return
    }
    
    // Process the response
    func handleTicket(ticket: GTLRServiceTicket,
                      finishedWithObject result : GTLRSheets_ValueRange,
                      error : NSError?){
        
        if let error = error {
            print("error: ")
            print(error.localizedDescription)
        }
        return
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
                emergencySpreadsheetId = (result.value(forKey: "string") as? String)!
            }
        }
        catch {
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
        
        request = NSFetchRequest<NSFetchRequestResult>(entityName: "PriorityPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                prioritySpreadsheetId = result.value(forKey: "string") as! String
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
        
        if segue.destination is OptionsNavigationController {
            (segue.destination as! OptionsNavigationController).passMainMenu(menu: self)
        }
    }

}
