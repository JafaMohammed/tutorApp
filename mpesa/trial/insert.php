<?php
/**
 * Created by PhpStorm.
 * User: dennis
 * Date: 1/28/2019
 * Time: 4:09 PM
 */
require_once './vendor/autoload.php';
use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;
class insert
{
    protected $database;
    protected $dbase ='yes';
    public function __construct()
    {
        $acc = ServiceAccount::fromJsonFile(__DIR__.'/another/kich-85a6c-189a3ac3d8a4.json');

        $firbase = (new Factory)->withServiceAccount($acc)->create();
        $this->database=$firbase->getDatabase();
    }
    public function writ(array $data){
        //if (empty($data) || isset($data)){return false;}

        foreach ($data as $key => $value){
            $this->database->getReference()->getChild($this->dbase)->getChild($key)->set($value);

        }
        return true;
    }
}
$dataPOST = trim(file_get_contents('php://input'));
$callbackJSONData=file_get_contents('php://input');
$callbackData=json_decode($callbackJSONData);
$resultCode=$callbackData->Body->stkCallback->ResultCode;
$resultDesc=$callbackData->Body->stkCallback->ResultDesc;
$merchantRequestID=$callbackData->Body->stkCallback->MerchantRequestID;
$checkoutRequestID=$callbackData->Body->stkCallback->CheckoutRequestID;

$amount=$callbackData->Body->stkCallback->CallbackMetadata->Item[0]->Value;
$mpesaReceiptNumber=$callbackData->Body->stkCallback->CallbackMetadata->Item[1]->Value;
$balance=$callbackData->stkCallback->Body->CallbackMetadata->Item[2]->Value;
$b2CUtilityAccountAvailableFunds=$callbackData->Body->stkCallback->CallbackMetadata->Item[3]->Value;
$transactionDate=$callbackData->Body->stkCallback->CallbackMetadata->Item[3]->Value;
$phoneNumber=$callbackData->Body->stkCallback->CallbackMetadata->Item[4]->Value;




print_r($dataPOST);
$Insert =new Insert();

var_dump($Insert->writ([


    'john' => [
        'support' => $resultCode,
        'sales' => $resultDesc,
    ],


]));