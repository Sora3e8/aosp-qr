var scanner = null;
function onScanSuccess(decodedText, decodedResult) {
  // handle the scanned code as you like, for example:
  scanner.stop();
  webview.QRdata(decodedText);
}



function init(){
  const config = { fps: 60, qrbox: { width: 150, height: 150 }, useBarCodeDetectorIfSupported: true };
  scanner = new Html5Qrcode("reader");
  scanner.start({ facingMode: "environment" }, config, onScanSuccess);
}