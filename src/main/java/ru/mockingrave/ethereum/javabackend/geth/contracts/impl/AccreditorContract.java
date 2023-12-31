package ru.mockingrave.ethereum.javabackend.geth.contracts.impl;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class AccreditorContract extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162001fe638038062001fe683398101604081905262000034916200043a565b604080516020808201909252600090528151908201207fc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a4701415620000af576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401620000a690620006ae565b60405180910390fd5b620000ca8160006200016d60201b62000b191790919060201c565b6000600682604051620000de919062000658565b90815260405190819003602090810190912080546001600160a01b031916331781558351909250620001199160018401919085019062000339565b5080546040517f752fd264651bf86dbaf675bbbd5f5610b82986ed715377fce47f11ba7a0b68af916200015d9160018501916001600160a01b03169086906200066d565b60405180910390a150506200078e565b6200018282826001600160e01b036200021a16565b15620001bc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401620000a690620006c0565b60028201805460018181018084556000938452602093849020855192949193620001ec9391019186019062000339565b500382600062000205846001600160e01b036200032e16565b81526020810191909152604001600020555050565b6002820154600090620002305750600062000328565b62000244826001600160e01b036200032e16565b620003246002850185600062000263876001600160e01b036200032e16565b815260200190815260200160002054815481106200027d57fe5b600091825260209182902001805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156200030f5780601f10620002e3576101008083540402835291602001916200030f565b820191906000526020600020905b815481529060010190602001808311620002f157829003601f168201915b50506001600160e01b036200032e1692505050565b1490505b92915050565b805160209091012090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200037c57805160ff1916838001178555620003ac565b82800160010185558215620003ac579182015b82811115620003ac5782518255916020019190600101906200038f565b50620003ba929150620003be565b5090565b620003db91905b80821115620003ba5760008155600101620003c5565b90565b600082601f830112620003f057600080fd5b8151620004076200040182620006f9565b620006d2565b915080825260208301602083018583830111156200042457600080fd5b6200043183828462000751565b50505092915050565b6000602082840312156200044d57600080fd5b81516001600160401b038111156200046457600080fd5b6200047284828501620003de565b949350505050565b62000485816200073f565b82525050565b600062000498826200072d565b620004a4818562000731565b9350620004b681856020860162000751565b620004c18162000784565b9093019392505050565b6000620004d8826200072d565b620004e481856200073a565b9350620004f681856020860162000751565b9290920192915050565b60008154600181166000811462000520576001811462000549576200058e565b607f600283041662000533818762000731565b60ff19841681529550506020850192506200058e565b6002820462000559818762000731565b9550620005668562000721565b60005b82811015620005875781548882015260019091019060200162000569565b8701945050505b505092915050565b6000620005a560328362000731565b7f43616e2774206465706c6f79206120636f6e74726163742e20536f757263652081527f49504653206861736820697320656d7074790000000000000000000000000000602082015260400192915050565b60006200060660298362000731565b7f537472696e675365743a206b657920616c72656164792065786973747320696e81527f20746865207365742e0000000000000000000000000000000000000000000000602082015260400192915050565b6000620006668284620004cb565b9392505050565b6060808252810162000680818662000500565b90506200069160208301856200047a565b8181036040830152620006a581846200048b565b95945050505050565b60208082528101620003288162000596565b602080825281016200032881620005f7565b6040518181016001600160401b0381118282101715620006f157600080fd5b604052919050565b60006001600160401b038211156200071057600080fd5b506020601f91909101601f19160190565b60009081526020902090565b5190565b90815260200190565b919050565b60006001600160a01b03821662000328565b60005b838110156200076e57818101518382015260200162000754565b838111156200077e576000848401525b50505050565b601f01601f191690565b611848806200079e6000396000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c8063b1d384281161008c578063d129f67511610066578063d129f675146101d6578063d2343b3d146101e9578063ec8014b0146101fc578063f40949ba1461020f576100ea565b8063b1d38428146101a8578063b3f7f0f8146101bb578063ce237f44146101c3576100ea565b80634ae7039e116100c85780634ae7039e1461013557806358476116146101555780635d6cb302146101755780637b2803cc14610188576100ea565b80632fe7ba20146100ef5780633bfa28431461010d57806342b81f8414610122575b600080fd5b6100f7610222565b60405161010491906115c0565b60405180910390f35b61012061011b366004610fe1565b6102fe565b005b6101206101303660046110c6565b610441565b610148610143366004610fa4565b6104ed565b60405161010491906115df565b610168610163366004610fa4565b6105cc565b60405161010491906115d1565b610168610183366004610fa4565b6105e4565b61019b610196366004610fa4565b6105f7565b60405161010491906115b2565b6101206101b6366004610fe1565b610655565b6100f761072b565b6101486101d1366004610fa4565b6107fd565b61019b6101e436600461105d565b61083c565b6101206101f73660046110c6565b61092c565b61019b61020a366004610fa4565b6109d8565b61019b61021d36600461105d565b610a17565b60606003600201805480602002602001604051908101604052809291908181526020016000905b828210156102f45760008481526020908190208301805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156102e05780601f106102b5576101008083540402835291602001916102e0565b820191906000526020600020905b8154815290600101906020018083116102c357829003601f168201915b505050505081526020019060010190610249565b5050505090505b90565b8233610309826105f7565b6001600160a01b0316146103385760405162461bcd60e51b815260040161032f906116ac565b60405180910390fd5b604080516020808201909252600090528251908301207fc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470141561038d5760405162461bcd60e51b815260040161032f906116bc565b61039e60008363ffffffff610b1916565b60006006836040516103b091906115a6565b9081526040516020918190038201902080546001600160a01b0319166001600160a01b03871617815586519092506103f091600184019190880190610e1a565b5080546040517f752fd264651bf86dbaf675bbbd5f5610b82986ed715377fce47f11ba7a0b68af916104329160018501916001600160a01b031690879061167b565b60405180910390a15050505050565b823361044c826105f7565b6001600160a01b0316146104725760405162461bcd60e51b815260040161032f906116ac565b61048360008463ffffffff610b9116565b61049f5760405162461bcd60e51b815260040161032f9061170c565b60006104ab8585610a17565b90506104b88582856102fe565b7fae3f45f2bce9e5ee80dee646bcbf76d62e79452088d6821bdfd170fef6e0d41085828686604051610432949392919061162b565b606061050060038363ffffffff610b9116565b61051c5760405162461bcd60e51b815260040161032f906116cc565b60078260405161052c91906115a6565b90815260408051602092819003830181206001908101805460029281161561010002600019011691909104601f810185900485028301850190935282825290929091908301828280156105c05780601f10610595576101008083540402835291602001916105c0565b820191906000526020600020905b8154815290600101906020018083116105a357829003601f168201915b50505050509050919050565b60006105de818363ffffffff610b9116565b92915050565b60006105de60038363ffffffff610b9116565b6000610609818363ffffffff610b9116565b6106255760405162461bcd60e51b815260040161032f9061169c565b60068260405161063591906115a6565b908152604051908190036020019020546001600160a01b03169050919050565b8233610660826105f7565b6001600160a01b0316146106865760405162461bcd60e51b815260040161032f906116ac565b61069760038363ffffffff610b1916565b60006007836040516106a991906115a6565b9081526040516020918190038201902080546001600160a01b0319166001600160a01b03871617815586519092506106e991600184019190880190610e1a565b5080546040517f189aac14c866eb5dc094848f9558954f19d86aa6acb0d14513de7c587b646233916104329160018501916001600160a01b031690879061167b565b60606000600201805480602002602001604051908101604052809291908181526020016000905b828210156102f45760008481526020908190208301805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156107e95780601f106107be576101008083540402835291602001916107e9565b820191906000526020600020905b8154815290600101906020018083116107cc57829003601f168201915b505050505081526020019060010190610752565b606061081060008363ffffffff610b9116565b61082c5760405162461bcd60e51b815260040161032f9061169c565b60068260405161052c91906115a6565b60008233610849826105f7565b6001600160a01b03161461086f5760405162461bcd60e51b815260040161032f906116ac565b61088060038463ffffffff610c7a16565b600060078460405161089291906115a6565b908152604051908190036020018120546001600160a01b031691506007906108bb9086906115a6565b90815260405190819003602001902080546001600160a01b031916815560006108e76001830182610e98565b50507fdfd3dac0750dc056e2551d26b260a16db0b958a580a2542b5aa07cb799fc1a8585828660405161091c939291906115f0565b60405180910390a1949350505050565b8233610937826105f7565b6001600160a01b03161461095d5760405162461bcd60e51b815260040161032f906116ac565b61096e60038463ffffffff610b9116565b61098a5760405162461bcd60e51b815260040161032f906116fc565b6000610996858561083c565b90506109a3858285610655565b7fbedd16595484b5b75b61e845efd6425790488fa158b8b3c0524270f922d6909285828686604051610432949392919061162b565b60006109eb60038363ffffffff610b9116565b610a075760405162461bcd60e51b815260040161032f906116cc565b60078260405161063591906115a6565b60008233610a24826105f7565b6001600160a01b031614610a4a5760405162461bcd60e51b815260040161032f906116ac565b600254600110610a6c5760405162461bcd60e51b815260040161032f906116dc565b610a7d60008463ffffffff610c7a16565b6000600684604051610a8f91906115a6565b908152604051908190036020018120546001600160a01b03169150600690610ab89086906115a6565b90815260405190819003602001902080546001600160a01b03191681556000610ae46001830182610e98565b50507fff7c27a3d1c538534ce789dd427514b5e45059352fb1b1c98a0bd890b16f56ac85828660405161091c939291906115f0565b610b238282610b91565b15610b405760405162461bcd60e51b815260040161032f906116ec565b60028201805460018181018084556000938452602093849020855192949193610b6e93910191860190610e1a565b5003826000610b7c84610e08565b81526020810191909152604001600020555050565b6002820154600090610ba5575060006105de565b610bae82610e08565b610c7260028501856000610bc187610e08565b81526020019081526020016000205481548110610bda57fe5b600091825260209182902001805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815292830182828015610c685780601f10610c3d57610100808354040283529160200191610c68565b820191906000526020600020905b815481529060010190602001808311610c4b57829003601f168201915b5050505050610e08565b149392505050565b610c848282610b91565b610ca05760405162461bcd60e51b815260040161032f9061168c565b60006001610cad84610e13565b03905060008381610cbd85610e08565b8152602001908152602001600020549050818114610dcd576060846002018381548110610ce657fe5b600091825260209182902001805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815292830182828015610d745780601f10610d4957610100808354040283529160200191610d74565b820191906000526020600020905b815481529060010190602001808311610d5757829003601f168201915b5050505050905081856000016000610d8b84610e08565b81526020019081526020016000208190555080856002018381548110610dad57fe5b906000526020600020019080519060200190610dca929190610e1a565b50505b836000610dd985610e08565b8152602081019190915260400160009081205560028401805490610e01906000198301610edf565b5050505050565b805160209091012090565b6002015490565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610e5b57805160ff1916838001178555610e88565b82800160010185558215610e88579182015b82811115610e88578251825591602001919060010190610e6d565b50610e94929150610f08565b5090565b50805460018160011615610100020316600290046000825580601f10610ebe5750610edc565b601f016020900490600052602060002090810190610edc9190610f08565b50565b815481835581811115610f0357600083815260209020610f03918101908301610f22565b505050565b6102fb91905b80821115610e945760008155600101610f0e565b6102fb91905b80821115610e94576000610f3c8282610e98565b50600101610f28565b80356105de816117f1565b600082601f830112610f6157600080fd5b8135610f74610f6f82611743565b61171c565b91508082526020830160208301858383011115610f9057600080fd5b610f9b8382846117ab565b50505092915050565b600060208284031215610fb657600080fd5b813567ffffffffffffffff811115610fcd57600080fd5b610fd984828501610f50565b949350505050565b600080600060608486031215610ff657600080fd5b833567ffffffffffffffff81111561100d57600080fd5b61101986828701610f50565b935050602061102a86828701610f45565b925050604084013567ffffffffffffffff81111561104757600080fd5b61105386828701610f50565b9150509250925092565b6000806040838503121561107057600080fd5b823567ffffffffffffffff81111561108757600080fd5b61109385828601610f50565b925050602083013567ffffffffffffffff8111156110b057600080fd5b6110bc85828601610f50565b9150509250929050565b6000806000606084860312156110db57600080fd5b833567ffffffffffffffff8111156110f257600080fd5b6110fe86828701610f50565b935050602084013567ffffffffffffffff81111561111b57600080fd5b61102a86828701610f50565b600061113383836111c0565b9392505050565b6111438161178f565b82525050565b60006111548261177d565b61115e8185611781565b9350836020820285016111708561176b565b8060005b858110156111aa578484038952815161118d8582611127565b94506111988361176b565b60209a909a0199925050600101611174565b5091979650505050505050565b6111438161179a565b60006111cb8261177d565b6111d58185611781565b93506111e58185602086016117b7565b6111ee816117e7565b9093019392505050565b60006112038261177d565b61120d818561178a565b935061121d8185602086016117b7565b9290920192915050565b600081546001811660008114611244576001811461126a576112a9565b607f60028304166112558187611781565b60ff19841681529550506020850192506112a9565b600282046112788187611781565b955061128385611771565b60005b828110156112a257815488820152600190910190602001611286565b8701945050505b505092915050565b60006112be602983611781565b7f537472696e675365743a206b657920646f6573206e6f7420657869737420696e815268103a34329039b2ba1760b91b602082015260400192915050565b6000611309602a83611781565b7f43616e27742067657420612061636372656469746f72207468617420646f65738152693713ba1032bc34b9ba1760b11b602082015260400192915050565b6000611355604383611781565b7f596f7520646f6e27742068617665207065726d697373696f6e2e2028596f752081527f617265206e6f74206f6e20746865206c697374206f662041636372656469746f60208201526272732960e81b604082015260600192915050565b60006113c0602e83611781565b7f43616e27742063726561746520616e2061636372656469746f722e204950465381526d206861736820697320656d70747960901b602082015260400192915050565b6000611410602983611781565b7f43616e277420676574206120636572746966696572207468617420646f65736e81526813ba1032bc34b9ba1760b91b602082015260400192915050565b600061145b602d83611781565b7f556e61626c6520746f2064656c6574652e20596f752061726520746865206c6181526c39ba1030b1b1b932b234ba37b960991b602082015260400192915050565b60006114aa602983611781565b7f537472696e675365743a206b657920616c72656164792065786973747320696e815268103a34329039b2ba1760b91b602082015260400192915050565b60006114f5603e83611781565b7f43616e27742075706461746520616e206365727469666965722773204950465381527f20686173682e204f6c64206861736820646f65736e27742065786973742e0000602082015260400192915050565b6000611554603f83611781565b7f43616e27742075706461746520616e2061636372656469746f7227732049504681527f5320686173682e204f6c64206861736820646f65736e27742065786973742e00602082015260400192915050565b600061113382846111f8565b602081016105de828461113a565b602080825281016111338184611149565b602081016105de82846111b7565b6020808252810161113381846111c0565b6060808252810161160181866111c0565b9050611610602083018561113a565b818103604083015261162281846111c0565b95945050505050565b6080808252810161163c81876111c0565b905061164b602083018661113a565b818103604083015261165d81856111c0565b9050818103606083015261167181846111c0565b9695505050505050565b606080825281016116018186611227565b602080825281016105de816112b1565b602080825281016105de816112fc565b602080825281016105de81611348565b602080825281016105de816113b3565b602080825281016105de81611403565b602080825281016105de8161144e565b602080825281016105de8161149d565b602080825281016105de816114e8565b602080825281016105de81611547565b60405181810167ffffffffffffffff8111828210171561173b57600080fd5b604052919050565b600067ffffffffffffffff82111561175a57600080fd5b506020601f91909101601f19160190565b60200190565b60009081526020902090565b5190565b90815260200190565b919050565b60006105de8261179f565b151590565b6001600160a01b031690565b82818337506000910152565b60005b838110156117d25781810151838201526020016117ba565b838111156117e1576000848401525b50505050565b601f01601f191690565b6117fa8161178f565b8114610edc57600080fdfea365627a7a72305820092ea96b1c6b60569eb406b7ba08bf72bdc94652e57132c860ba876174fffd1b6c6578706572696d656e74616cf564736f6c634300050a0040";

    public static final String FUNC_GETALLCERTIFIERS = "getAllCertifiers";

    public static final String FUNC_CREATEACCREDITOR = "createAccreditor";

    public static final String FUNC_UPDATEACCREDITOR = "updateAccreditor";

    public static final String FUNC_GETCERTIFIERSOURCE = "getCertifierSource";

    public static final String FUNC_CHECKACCREDITOR = "checkAccreditor";

    public static final String FUNC_CHECKCERTIFIER = "checkCertifier";

    public static final String FUNC_GETACCREDITORADDRESS = "getAccreditorAddress";

    public static final String FUNC_CREATECERTIFIER = "createCertifier";

    public static final String FUNC_GETALLACCREDITORS = "getAllAccreditors";

    public static final String FUNC_GETACCREDITORSOURCE = "getAccreditorSource";

    public static final String FUNC_DELETECERTIFIER = "deleteCertifier";

    public static final String FUNC_UPDATECERTIFIER = "updateCertifier";

    public static final String FUNC_GETCERTIFIERADDRESS = "getCertifierAddress";

    public static final String FUNC_DELETEACCREDITOR = "deleteAccreditor";

    public static final Event LOGCREATEACCREDITOR_EVENT = new Event("LogCreateAccreditor",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGCREATECERTIFIER_EVENT = new Event("LogCreateCertifier",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGUPDATEACCREDITOR_EVENT = new Event("LogUpdateAccreditor",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGUPDATECERTIFIER_EVENT = new Event("LogUpdateCertifier",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGDELETEACCREDITOR_EVENT = new Event("LogDeleteAccreditor",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGDELETECERTIFIER_EVENT = new Event("LogDeleteCertifier",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected AccreditorContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AccreditorContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AccreditorContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AccreditorContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    protected TransactionReceipt executeRemoteCallTransaction(Function function, Credentials credentials){
        String txData = FunctionEncoder.encode(function);

        try {
            TransactionManager txManager =new RawTransactionManager(web3j, credentials, Long.parseLong(web3j.netVersion().send().getNetVersion()));
             String txHash = txManager.sendTransaction(
                     BigInteger.valueOf(0),
                    DefaultGasProvider.GAS_LIMIT,
                    this.getContractAddress(),
                    txData, BigInteger.ZERO).getTransactionHash();
            TransactionReceiptProcessor receiptProcessor =
                    new PollingTransactionReceiptProcessor(web3j, TransactionManager.DEFAULT_POLLING_FREQUENCY,
                            TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            return receiptProcessor.waitForTransactionReceipt(txHash);
        } catch (IOException | TransactionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RemoteFunctionCall<List> getAllCertifiers() {
        final Function function = new Function(FUNC_GETALLCERTIFIERS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> createAccreditor(String sourceIpfsHash, String newAccreditor, String newIpfsHash) {
        final Function function = new Function(
                FUNC_CREATEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Address(160, newAccreditor),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt createAccreditor
            (String sourceIpfsHash, String newAccreditor, String newIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_CREATEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Address(160, newAccreditor),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());

        return executeRemoteCallTransaction(function, credentials);
    }
    public RemoteFunctionCall<TransactionReceipt> updateAccreditor(String sourceIpfsHash, String oldIpfsHash, String newIpfsHash) {
        final Function function = new Function(
                FUNC_UPDATEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(oldIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt updateAccreditor
            (String sourceIpfsHash, String oldIpfsHash, String newIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_UPDATEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(oldIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<String> getCertifierSource(String ipfsHash) {
        final Function function = new Function(FUNC_GETCERTIFIERSOURCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> checkAccreditor(String ipfsHash) {
        final Function function = new Function(FUNC_CHECKACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> checkCertifier(String ipfsHash) {
        final Function function = new Function(FUNC_CHECKCERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getAccreditorAddress(String ipfsHash) {
        final Function function = new Function(FUNC_GETACCREDITORADDRESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createCertifier(String sourceIpfsHash, String newCertifier, String newIpfsHash) {
        final Function function = new Function(
                FUNC_CREATECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Address(160, newCertifier),
                new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt createCertifier
            (String sourceIpfsHash, String newCertifier, String newIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_CREATECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Address(160, newCertifier),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<List> getAllAccreditors() {
        final Function function = new Function(FUNC_GETALLACCREDITORS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getAccreditorSource(String ipfsHash) {
        final Function function = new Function(FUNC_GETACCREDITORSOURCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteCertifier(String sourceIpfsHash, String deleteIpfsHash) {
        final Function function = new Function(
                FUNC_DELETECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }
    public TransactionReceipt deleteCertifier
            (String sourceIpfsHash, String deleteIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_DELETECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<TransactionReceipt> updateCertifier(String sourceIpfsHash, String oldIpfsHash, String newIpfsHash) {
        final Function function = new Function(
                FUNC_UPDATECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(oldIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }
    public TransactionReceipt updateCertifier
            (String sourceIpfsHash, String oldIpfsHash, String newIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_UPDATECERTIFIER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(oldIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<String> getCertifierAddress(String ipfsHash) {
        final Function function = new Function(FUNC_GETCERTIFIERADDRESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteAccreditor(String sourceIpfsHash, String deleteIpfsHash) {
        final Function function = new Function(
                FUNC_DELETEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt deleteAccreditor(String sourceIpfsHash, String deleteIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_DELETEACCREDITOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public List<LogCreateAccreditorEventResponse> getLogCreateAccreditorEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGCREATEACCREDITOR_EVENT, transactionReceipt);
        ArrayList<LogCreateAccreditorEventResponse> responses = new ArrayList<LogCreateAccreditorEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogCreateAccreditorEventResponse typedResponse = new LogCreateAccreditorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogCreateAccreditorEventResponse> logCreateAccreditorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogCreateAccreditorEventResponse>() {
            @Override
            public LogCreateAccreditorEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGCREATEACCREDITOR_EVENT, log);
                LogCreateAccreditorEventResponse typedResponse = new LogCreateAccreditorEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogCreateAccreditorEventResponse> logCreateAccreditorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGCREATEACCREDITOR_EVENT));
        return logCreateAccreditorEventFlowable(filter);
    }

    public List<LogCreateCertifierEventResponse> getLogCreateCertifierEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGCREATECERTIFIER_EVENT, transactionReceipt);
        ArrayList<LogCreateCertifierEventResponse> responses = new ArrayList<LogCreateCertifierEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogCreateCertifierEventResponse typedResponse = new LogCreateCertifierEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogCreateCertifierEventResponse> logCreateCertifierEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogCreateCertifierEventResponse>() {
            @Override
            public LogCreateCertifierEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGCREATECERTIFIER_EVENT, log);
                LogCreateCertifierEventResponse typedResponse = new LogCreateCertifierEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.newCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogCreateCertifierEventResponse> logCreateCertifierEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGCREATECERTIFIER_EVENT));
        return logCreateCertifierEventFlowable(filter);
    }

    public List<LogUpdateAccreditorEventResponse> getLogUpdateAccreditorEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGUPDATEACCREDITOR_EVENT, transactionReceipt);
        ArrayList<LogUpdateAccreditorEventResponse> responses = new ArrayList<LogUpdateAccreditorEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogUpdateAccreditorEventResponse typedResponse = new LogUpdateAccreditorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.editAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.oldIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.newIpfsHash = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogUpdateAccreditorEventResponse> logUpdateAccreditorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogUpdateAccreditorEventResponse>() {
            @Override
            public LogUpdateAccreditorEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGUPDATEACCREDITOR_EVENT, log);
                LogUpdateAccreditorEventResponse typedResponse = new LogUpdateAccreditorEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.editAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.oldIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.newIpfsHash = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogUpdateAccreditorEventResponse> logUpdateAccreditorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGUPDATEACCREDITOR_EVENT));
        return logUpdateAccreditorEventFlowable(filter);
    }

    public List<LogUpdateCertifierEventResponse> getLogUpdateCertifierEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGUPDATECERTIFIER_EVENT, transactionReceipt);
        ArrayList<LogUpdateCertifierEventResponse> responses = new ArrayList<LogUpdateCertifierEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogUpdateCertifierEventResponse typedResponse = new LogUpdateCertifierEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.editCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.oldIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.newIpfsHash = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogUpdateCertifierEventResponse> logUpdateCertifierEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogUpdateCertifierEventResponse>() {
            @Override
            public LogUpdateCertifierEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGUPDATECERTIFIER_EVENT, log);
                LogUpdateCertifierEventResponse typedResponse = new LogUpdateCertifierEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.editCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.oldIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.newIpfsHash = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogUpdateCertifierEventResponse> logUpdateCertifierEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGUPDATECERTIFIER_EVENT));
        return logUpdateCertifierEventFlowable(filter);
    }

    public List<LogDeleteAccreditorEventResponse> getLogDeleteAccreditorEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGDELETEACCREDITOR_EVENT, transactionReceipt);
        ArrayList<LogDeleteAccreditorEventResponse> responses = new ArrayList<LogDeleteAccreditorEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogDeleteAccreditorEventResponse typedResponse = new LogDeleteAccreditorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.deleteAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogDeleteAccreditorEventResponse> logDeleteAccreditorEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogDeleteAccreditorEventResponse>() {
            @Override
            public LogDeleteAccreditorEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGDELETEACCREDITOR_EVENT, log);
                LogDeleteAccreditorEventResponse typedResponse = new LogDeleteAccreditorEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.deleteAccreditor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogDeleteAccreditorEventResponse> logDeleteAccreditorEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGDELETEACCREDITOR_EVENT));
        return logDeleteAccreditorEventFlowable(filter);
    }

    public List<LogDeleteCertifierEventResponse> getLogDeleteCertifierEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGDELETECERTIFIER_EVENT, transactionReceipt);
        ArrayList<LogDeleteCertifierEventResponse> responses = new ArrayList<LogDeleteCertifierEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogDeleteCertifierEventResponse typedResponse = new LogDeleteCertifierEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.deleteCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogDeleteCertifierEventResponse> logDeleteCertifierEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogDeleteCertifierEventResponse>() {
            @Override
            public LogDeleteCertifierEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGDELETECERTIFIER_EVENT, log);
                LogDeleteCertifierEventResponse typedResponse = new LogDeleteCertifierEventResponse();
                typedResponse.log = log;
                typedResponse.sourceIpfsHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.deleteCertifier = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogDeleteCertifierEventResponse> logDeleteCertifierEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGDELETECERTIFIER_EVENT));
        return logDeleteCertifierEventFlowable(filter);
    }

    @Deprecated
    public static AccreditorContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccreditorContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AccreditorContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccreditorContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AccreditorContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AccreditorContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AccreditorContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AccreditorContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AccreditorContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String ipfsHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)));
        return deployRemoteCall(AccreditorContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<AccreditorContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String ipfsHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)));
        return deployRemoteCall(AccreditorContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AccreditorContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String ipfsHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)));
        return deployRemoteCall(AccreditorContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AccreditorContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String ipfsHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)));
        return deployRemoteCall(AccreditorContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class LogCreateAccreditorEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String newAccreditor;

        public String ipfsHash;
    }

    public static class LogCreateCertifierEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String newCertifier;

        public String ipfsHash;
    }

    public static class LogUpdateAccreditorEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String editAccreditor;

        public String oldIpfsHash;

        public String newIpfsHash;
    }

    public static class LogUpdateCertifierEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String editCertifier;

        public String oldIpfsHash;

        public String newIpfsHash;
    }

    public static class LogDeleteAccreditorEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String deleteAccreditor;

        public String deleteIpfsHash;
    }

    public static class LogDeleteCertifierEventResponse extends BaseEventResponse {
        public String sourceIpfsHash;

        public String deleteCertifier;

        public String deleteIpfsHash;
    }


}
