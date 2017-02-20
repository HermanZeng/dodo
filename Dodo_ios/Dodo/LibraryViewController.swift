//
//  DashboardViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/10/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import XLPagerTabStrip
import ChameleonFramework

class LibraryViewController: TwitterPagerTabStripViewController {

    var isReload = false
    

    override func viewDidLoad() {
        self.navigationController?.hidesNavigationBarHairline = true
        self.navigationController?.navigationBar.tintColor = FlatWhite()
        
        self.view?.backgroundColor = FlatWhite()
        super.viewDidLoad()
    }
    

    override func viewControllersForPagerTabStrip(pagerTabStripController: PagerTabStripViewController) -> [UIViewController] {
        let child_1 = TracksViewController.instantiateFromStoryboard()
        let child_2 = BooksViewController.instantiateFromStoryboard()
  
        return [child_1, child_2]
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
