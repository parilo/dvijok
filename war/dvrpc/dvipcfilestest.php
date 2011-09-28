<?php

require_once "dvipcfiles.php";

class DVIPCFilesTest extends DVIPCFiles {
	
	public function __construct(){
		parent::__construct();
 		$this->putEnvToBus("aaaa", "aaaaval");
		print "env: ".$this->getEnvFromBus("aaaa")."\n";
 		$this->removeEnvFromBus("aaaa");

 		$this->putToQueue("aa", "aaa1");
//  		sleep(1);
 		$this->putToQueue("aa", "aaa2");
//  		sleep(1);
 		$this->putToQueue("aa", "aaa3");
 		$this->putToQueue("aa", "aaa4");
 		$this->putToQueue("aa", "aaa5");
 		
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
 		print "queue: ".$this->getFromQueue("aa")."\n";
// 		protected function getFromQueue($name);
// 		protected function putToQueue($name, $val);
		
	}
	
}

new DVIPCFilesTest();

?>
