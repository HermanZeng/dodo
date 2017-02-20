//
//  LoginViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/4/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController, UITextFieldDelegate {

    var userEmail: String?
    var userPwd: String?
    
    var provider: Networking!
    
    struct StoryBoard {
        static let main = "Login Successfully"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.dodo.style.bar.hideAfterDelaySeconds = 3
        provider = Networking.newDefaultNetworking()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var emailTextField: UITextField! {
        didSet {
            emailTextField.delegate = self
            emailTextField.text = userEmail
        }
    }
    
    @IBOutlet weak var pwdTextField: UITextField! {
        didSet {
            pwdTextField.delegate = self
            pwdTextField.text = userPwd
        }
    }
    
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    // entering
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField == emailTextField {
            userEmail = emailTextField.text
            pwdTextField.becomeFirstResponder()
        } else if textField == pwdTextField {
            userPwd = pwdTextField.text
            pwdTextField.resignFirstResponder()
            signIn()
        }
        return true
    }
    
    func textFieldShouldBeginEditing(textField: UITextField) -> Bool {
        if textField.textColor == UIColor.redColor() {
            textField.text? = ""
        }
        textField.textColor = UIColor.blackColor()
        if textField == pwdTextField {
            textField.secureTextEntry = true
        }
        return true
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        if textField == emailTextField {
            userEmail = emailTextField.text
        } else if textField == pwdTextField {
            userPwd = pwdTextField.text
        }
    }
    
    
    private func signIn() {
        // TODO
        if validate() {
            print("Sign in !!")
            let login: DodoApi = DodoApi.XAuth(email: userEmail!, password: userPwd!)
            spinner.startAnimating()
            provider.request(login)
                .mapJSON()
                .subscribe { (event) -> Void in
                    switch event {
                    case .Next(let response):
                            print(response)

                        if let token = response["token"] as? String{
                            if let user = response["user"] {
                                let firstname = user!["firstname"] as! String
                                let lastname = user!["lastname"] as! String
                                self.saveToken(token, firstname: firstname, lastname: lastname)
                            }
                        } else if let status = response["status"] as? Int {
                            switch status {
                            case 400:
                                print("404\n")
                            case 401:
                                setInvalid(self.pwdTextField, message: Constants.Error.WrongPassword)
                            case 404:
                                setInvalid(self.emailTextField, message: Constants.Error.WrongEmail)
                            default:
                                break
                            }
                        }
                        self.spinner.stopAnimating()
                    case .Error(let error):
                        self.spinner.stopAnimating()
                        print("Check your internet connection")
                        print("ENTER ERROR FIELD!!!error. \(error)")
                        NetworkSucks(self.view)
                        
                    default:
                        print("enter default")
                    }
                }
        }
    }
    
    private func saveToken(token: String, firstname: String, lastname: String) {
        var appToken = XAppToken()
        appToken.token = token
        appToken.email = userEmail
        appToken.password = userPwd
        appToken.firstname = firstname
        appToken.lastname = lastname
        appToken.loginState = true
        performSegueWithIdentifier(StoryBoard.main, sender: nil)
    }
    
    private func validate() -> Bool {
        var valid = true
        if !isValidEmail(userEmail ?? "") {
            emailTextField.text = Constants.Error.InvalidEmail
            emailTextField.textColor = UIColor.redColor()
            valid = false
        }
        if !isValidPassword(userPwd ?? "") {
            pwdTextField.text = Constants.Error.InvalidPassword
            pwdTextField.secureTextEntry = false
            pwdTextField.textColor = UIColor.redColor()
            valid = false
        }
        return valid
    }
    
    
    
    
    // SIGN IN
    
    @IBAction func doSignIn(sender: UIButton) {
        emailTextField.resignFirstResponder()
        pwdTextField.resignFirstResponder()
        signIn()
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if let nvc = segue.destinationViewController as? RegisterViewController {
            nvc.provider = self.provider
        }
    }
    
    override func supportedInterfaceOrientations() -> UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.Portrait
    }
    
    override func shouldAutorotate() -> Bool {
        return true
    }
    

}
