//
//  SMBSessionDownloadTaskDelegate.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 9/1/17.
//  Copyright © 2017 Nathan Walker. All rights reserved.
//

import Foundation

class SMBSessionDownloadTaskDelegate: NSObject, TOSMBSessionDownloadTaskDelegate {
    
    var progress: Float = 0
    
    
    
    override func downloadTask(_ downloadTask: TOSMBSessionDownloadTask!, didWriteBytes bytesWritten: UInt64, totalBytesReceived: UInt64, totalBytesExpectedToReceive totalBytesToReceive: Int64) {
        self.progress = Float(totalBytesReceived) / Float(totalBytesToReceive)
    }
    
    
//    - (void)downloadTask:(TOSMBSessionDownloadTask *)downloadTask didFinishDownloadingToPath:(NSString *)destinationPath
//    {
//    [self updateUiToDownloadFinishedState];
//    self.filePath = destinationPath;
//    }
//    
//    - (void)downloadTask:(TOSMBSessionDownloadTask *)downloadTask didCompleteWithError:(NSError *)error
//    {
//    [self updateUiToDownloadFinishedState];
//    [[[UIAlertView alloc] initWithTitle:@"SMB Client Error" message:error.localizedDescription delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil] show];
//    }


    
}
