//
//  RegisterViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/4/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import SwiftyJSON

class RegisterViewController: UIViewController, UITextFieldDelegate {
    
    private var userFirstName: String?
    private var userLastName: String?
    private var userEmail: String?
    private var userPwd: String?
    
    struct StoryBoard {
        static let gotoLogin = "Goto Login"
    }
    var provider: Networking!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBOutlet weak var firstnameView: UITextField! {
        didSet {
            firstnameView.delegate = self
        }
    }
    @IBOutlet weak var lastnameView: UITextField! {
        didSet {
            lastnameView.delegate = self
        }
    }
    
    @IBOutlet weak var emailTextField: UITextField! {
        didSet {
            emailTextField.delegate = self
        }
    }

    @IBOutlet weak var pwdTextField: UITextField! {
        didSet {
            pwdTextField.delegate = self
        }
    }
    
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        switch textField {
        case firstnameView:
            lastnameView.becomeFirstResponder()
        case lastnameView:
            emailTextField.becomeFirstResponder()
        case emailTextField:
            pwdTextField.becomeFirstResponder()
        case pwdTextField:
            pwdTextField.resignFirstResponder()
            register()
        default:
            break
        }
        return true
    }
    
    private func register() {
        if validate() {
            print("Sign in !!")
            let register: DodoApi = DodoApi.Register(firstname: userFirstName!, lastname: userLastName!, email: userEmail!, password: userPwd!)
            spinner.startAnimating()
            provider.request(register)
                .subscribe { (event) -> Void in
                    switch event {
                    case .Next(let response):
                        print(response)
                        let json = JSON(data: response.data)
                        if let status = json["status"].int {
                            switch status {
                            case 400:
                                print("404\n")
                            case 401:
                                setInvalid(self.pwdTextField, message: Constants.Error.WrongPassword)
                            case 404:
                                setInvalid(self.emailTextField, message: Constants.Error.WrongEmail)
                            case 409:
                                setInvalid(self.emailTextField, message: Constants.Error.ExistingEmail)
                            default:
                                break
                            }
                        } else {
                            self.successfullyRegistered()
                        }
                        self.spinner.stopAnimating()
                    case .Error(let error):
                        self.spinner.stopAnimating()
                        NetworkSucks(self.view)
                        print("error. \(error)")
                    default:
                        break
                    }
            }
        }
    }
    private func successfullyRegistered() {
        let alert = UIAlertController(title: "Wow!", message:"Congratrulations! Now please go to your email and do the final step to validate your account!", preferredStyle: .Alert)
        alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in self.gotoLogin()})
        self.presentViewController(alert, animated: true){}
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if let nvc = segue.destinationViewController as? LoginViewController {
            nvc.userEmail = self.userEmail
            nvc.userPwd = self.userPwd
        }
    }
    
    private func gotoLogin() {
        performSegueWithIdentifier(StoryBoard.gotoLogin, sender: nil)
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
        if userFirstName == nil {
            firstnameView.text = Constants.Error.InvalidName
            firstnameView.textColor = UIColor.redColor()
            valid = false
        }
        if userLastName == nil {
            lastnameView.text = Constants.Error.InvalidName
            lastnameView.textColor = UIColor.redColor()
            valid = false
        }
        return valid
    }
    
    
    func textFieldShouldBeginEditing(textField: UITextField) -> Bool {
        textField.text? = ""
        textField.textColor = UIColor.blackColor()
        if textField == pwdTextField {
            textField.secureTextEntry = true
        }
        return true
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        switch textField {
        case emailTextField:
            userEmail = emailTextField.text
        case pwdTextField:
            userPwd = pwdTextField.text
        case firstnameView:
            userFirstName = firstnameView.text
        case lastnameView:
            userLastName = lastnameView.text
        default:
            break
        }
    }
    
    @IBAction func doRegister(sender: UIButton) {
        emailTextField.resignFirstResponder()
        pwdTextField.resignFirstResponder()
        firstnameView.resignFirstResponder()
        lastnameView.resignFirstResponder()
        register()
    }

    
}
