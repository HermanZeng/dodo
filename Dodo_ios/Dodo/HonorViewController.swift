//
//  HonorViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 9/12/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import ChameleonFramework

class HonorViewController: UICollectionViewController {
    var provider: Networking! = Networking.newDefaultNetworking()
    var honors = [Honor]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getHonors()
    }
    
    private func getHonors() {
        provider.request(DodoApi.ListHonors)
                .mapJSON()
                .mapToObjectArray(Honor.self)
                .subscribeNext { [weak weakSelf=self]honors in
                    weakSelf?.honors = honors
                    weakSelf?.collectionView?.reloadData()
        }
    }
    
    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.honors.count
    }
    
    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("HonorCell", forIndexPath: indexPath)
        if let cell = cell as? HonorCell {
            cell.honorTitleView?.text = self.honors[indexPath.row].title
            cell.starView?.tintColor = UIColor.randomFlatColor()
        }
        return cell
    }
}
