<?php

require_once "dvipcfiles.php";

class DVIPCFilesTest extends DVIPCFiles {
	
	public function __construct(){
		parent::__construct();
//  		$this->putEnvToBus("aaaa", "aaaaval");
// 		print "env: ".$this->getEnvFromBus("aaaa")."\n";
//  		$this->removeEnvFromBus("aaaa");

//  		$this->putToQueue("aa", "aaa1");
//  		$this->putToQueue("aa", "aaa2");
//  		$this->putToQueue("aa", "aaa3");
//  		$this->putToQueue("aa", "aaa4");
//  		$this->putToQueue("aa", "aaa5");
 		
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";

		$this->invokeEvent("123event123");
		
	}
	
}

new DVIPCFilesTest();

?>
