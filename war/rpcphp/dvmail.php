<?php 

require_once 'Mail.php';
require_once("Mail/mime.php");

class DVMail {

	private $host;
	private $port;
	private $username;
	private $password;

	public function setLoginData($data){
		$this->host = $data['host'];
		$this->port = $data['port'];
		$this->username = $data['username'];
		$this->password = $data['password'];
	}
	
	/**
	 * 
	 * @param array $data
	 * 				$data['from']
	 * 				$data['to']
	 * 				$data['subject']
	 * 				$data['body']
	 * 
	 */
	public function sendMail($data){
	
		$from     = $data['from'];		//"<from.gmail.com>";
		$to       = $data['to'];		//"<to.yahoo.com>";
		$subject  = $data['subject'];	//"Hi!";
		$body     = $data['body'];		//"Hi,\n\nHow are you?";
		
		$headers = array(
				'From'    => $from,
				'To'      => $to,
				'Subject' => $subject
// 				'Errors-To' => $from,
// 				'Return-Path' => $from
		);
		
		$message = new Mail_mime();
		$message->setTXTBody($body);
// 		$message->setHTMLBody($messageHTML);
		
		$mimeparams=array();
		$mimeparams['text_encoding']="7bit";
		$mimeparams['text_charset']="UTF-8";
		$mimeparams['html_charset']="UTF-8";
		$mimeparams['head_charset']="UTF-8";
		
		$body = $message->get($mimeparams);
		$headers = $message->headers($headers);
		
		$smtp = Mail::factory('smtp', array(
				'host'     => $this->host,
				'port'     => $this->port,
				'auth'     => true,
				'username' => $this->username,
				'password' => $this->password
		));
		
		$mail = $smtp->send($to, $headers, $body);
		
		if (PEAR::isError($mail)) {
			error_log($mail->getMessage());
		}
	
	}

}

?>
