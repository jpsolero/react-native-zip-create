import Foundation
import Zip


@objc(ZipCreate)
class ZipCreate: RCTEventEmitter {

  @objc(zip:destinationPath:resolve:reject:)
  func zip(from: [URL], destinationPath: URL, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
    do {
        try Zip.zipFiles(paths: from, zipFilePath: destinationPath, password: nil, progress: { (progress) -> () in
            self.sendEvent(withName: "zipCreateProgress", body: ["progress": progress])
        })

        print("end")

        resolve(destinationPath.absoluteString)
    }
    catch {
      print ("error zipping")
      let error = NSError(domain: "", code: 200, userInfo: nil)
      reject("E_COUNT", "error zipping", error)
    }
  }

  override func supportedEvents() -> [String]! {
    return ["zipCreateProgress"]
  }
}
