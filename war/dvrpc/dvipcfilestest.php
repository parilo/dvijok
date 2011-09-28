<?php

require_once "dvipcfiles.php";

class DVIPCFilesTest extends DVIPCFiles {
	
	public function __construct(){
		parent::__construct();
 		$this->putEnvToBus("aaaa", "aaaaval");
		print "env: ".$this->getEnvFromBus("aaaa")."\n";
 		$this->removeEnvFromBus("aaaa");

// 		protected function getFromQueue($name);
// 		protected function putToQueue($name, $val);
		
	}
	
}

new DVIPCFilesTest();

?>
