package ru.mockingrave.ethereum.javabackend.geth.contracts.impl;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
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
public class CertifierContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506040516110dc3803806110dc8339818101604052602081101561003357600080fd5b5051600080546001600160a01b039092166001600160a01b0319909216919091179055611077806100656000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063405b15031461005c5780635e2d999514610187578063ed0f2e75146102b0578063f02c5a20146103c9578063ffbd4dd5146104f2575b600080fd5b6101856004803603604081101561007257600080fd5b810190602081018135600160201b81111561008c57600080fd5b82018360208201111561009e57600080fd5b803590602001918460018302840111600160201b831117156100bf57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561011157600080fd5b82018360208201111561012357600080fd5b803590602001918460018302840111600160201b8311171561014457600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506105aa945050505050565b005b6101856004803603604081101561019d57600080fd5b810190602081018135600160201b8111156101b757600080fd5b8201836020820111156101c957600080fd5b803590602001918460018302840111600160201b831117156101ea57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561023c57600080fd5b82018360208201111561024e57600080fd5b803590602001918460018302840111600160201b8311171561026f57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061083a945050505050565b610354600480360360208110156102c657600080fd5b810190602081018135600160201b8111156102e057600080fd5b8201836020820111156102f257600080fd5b803590602001918460018302840111600160201b8311171561031357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610a32945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561038e578181015183820152602001610376565b50505050905090810190601f1680156103bb5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610185600480360360408110156103df57600080fd5b810190602081018135600160201b8111156103f957600080fd5b82018360208201111561040b57600080fd5b803590602001918460018302840111600160201b8311171561042c57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561047e57600080fd5b82018360208201111561049057600080fd5b803590602001918460018302840111600160201b831117156104b157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610b6a945050505050565b6105966004803603602081101561050857600080fd5b810190602081018135600160201b81111561052257600080fd5b82018360208201111561053457600080fd5b803590602001918460018302840111600160201b8311171561055557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610dd6945050505050565b604080519115158252519081900360200190f35b60008054604051630ec8014b60e41b8152602060048201818152865160248401528651879533956001600160a01b03169463ec8014b0948894938493604490930192918601918190849084905b8381101561060f5781810151838201526020016105f7565b50505050905090810190601f16801561063c5780820380516001836020036101000a031916815260200191505b509250505060206040518083038186803b15801561065957600080fd5b505afa15801561066d573d6000803e3d6000fd5b505050506040513d602081101561068357600080fd5b50516001600160a01b0316146106ca5760405162461bcd60e51b81526004018080602001828103825260428152602001806110016042913960600191505060405180910390fd5b6001826040518082805190602001908083835b602083106106fc5780518252601f1990920191602091820191016106dd565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220610735925090506000610ec5565b7fca96b22708457f98df985d632fff7c17d94b52c2cf3061e7dd1cccadbc72e59c8383604051808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610799578181015183820152602001610781565b50505050905090810190601f1680156107c65780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156107f95781810151838201526020016107e1565b50505050905090810190601f1680156108265780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a1505050565b60008054604051630ec8014b60e41b8152602060048201818152865160248401528651879533956001600160a01b03169463ec8014b0948894938493604490930192918601918190849084905b8381101561089f578181015183820152602001610887565b50505050905090810190601f1680156108cc5780820380516001836020036101000a031916815260200191505b509250505060206040518083038186803b1580156108e957600080fd5b505afa1580156108fd573d6000803e3d6000fd5b505050506040513d602081101561091357600080fd5b50516001600160a01b03161461095a5760405162461bcd60e51b81526004018080602001828103825260428152602001806110016042913960600191505060405180910390fd5b826001836040518082805190602001908083835b6020831061098d5780518252601f19909201916020918201910161096e565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516109ce9591949190910192509050610f0c565b507f1fdcf2a224dba63cbf7dcc3a24e1ddb6816450a7fe14ddb2d28b6a63142f058383836040518080602001806020018381038352858181518152602001915080519060200190808383600083811015610799578181015183820152602001610781565b6060610a3d82610dd6565b610a785760405162461bcd60e51b815260040180806020018281038252602b815260200180610fa8602b913960400191505060405180910390fd5b6001826040518082805190602001908083835b60208310610aaa5780518252601f199092019160209182019101610a8b565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f6002600183161590980290950116959095049283018290048202880182019052818752929450925050830182828015610b5e5780601f10610b3357610100808354040283529160200191610b5e565b820191906000526020600020905b815481529060010190602001808311610b4157829003601f168201915b50505050509050919050565b60008054604051630ec8014b60e41b8152602060048201818152865160248401528651879533956001600160a01b03169463ec8014b0948894938493604490930192918601918190849084905b83811015610bcf578181015183820152602001610bb7565b50505050905090810190601f168015610bfc5780820380516001836020036101000a031916815260200191505b509250505060206040518083038186803b158015610c1957600080fd5b505afa158015610c2d573d6000803e3d6000fd5b505050506040513d6020811015610c4357600080fd5b50516001600160a01b031614610c8a5760405162461bcd60e51b81526004018080602001828103825260428152602001806110016042913960600191505060405180910390fd5b604080516020808201909252600090528251908301207fc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a4701415610cfe5760405162461bcd60e51b815260040180806020018281038252602e815260200180610fd3602e913960400191505060405180910390fd5b826001836040518082805190602001908083835b60208310610d315780518252601f199092019160209182019101610d12565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381019093208451610d729591949190910192509050610f0c565b507f130861be50d1f7d38946f388ce325c0ee9972e42f7bd94bbc3b9e92e6c32b4cb83836040518080602001806020018381038352858181518152602001915080519060200190808383600083811015610799578181015183820152602001610781565b600060405180602001604052806000815250805190602001206001836040518082805190602001908083835b60208310610e215780518252601f199092019160209182019101610e02565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206040518082805460018160011615610100020316600290048015610eaf5780601f10610e8d576101008083540402835291820191610eaf565b820191906000526020600020905b815481529060010190602001808311610e9b575b5050915050604051809103902014159050919050565b50805460018160011615610100020316600290046000825580601f10610eeb5750610f09565b601f016020900490600052602060002090810190610f099190610f8a565b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610f4d57805160ff1916838001178555610f7a565b82800160010185558215610f7a579182015b82811115610f7a578251825591602001919060010190610f5f565b50610f86929150610f8a565b5090565b610fa491905b80821115610f865760008155600101610f90565b9056fe43616e2774206765742061206365727469666963617465207468617420646f65736e27742065786973742e43616e27742063726561746520612063657274696669636174652e2049504653206861736820697320656d707479596f7520646f6e27742068617665207065726d697373696f6e2e2028596f7520617265206e6f74206f6e20746865206c697374206f66204365727469666965727329a265627a7a7230582052140969ffeb973758b42e361ae8c58fd9faa36280ca123d110bb3931116e2e064736f6c634300050a0032";

    public static final String FUNC_DELETECERTIFICATE = "deleteCertificate";

    public static final String FUNC_UPDATECERTIFICATESOURCE = "updateCertificateSource";

    public static final String FUNC_GETCERTIFICATE = "getCertificate";

    public static final String FUNC_CREATECERTIFICATE = "createCertificate";

    public static final String FUNC_CHECKCERTIFICATE = "checkCertificate";

    public static final Event LOGCREATECERTIFICATE_EVENT = new Event("LogCreateCertificate",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGUPDATECERTIFICATE_EVENT = new Event("LogUpdateCertificate",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGDELETECERTIFICATE_EVENT = new Event("LogDeleteCertificate",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected CertifierContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CertifierContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CertifierContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CertifierContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<TransactionReceipt> deleteCertificate(String sourceIpfsHash, String deleteIpfsHash) {
        final Function function = new Function(
                FUNC_DELETECERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt deleteCertificate(String sourceIpfsHash, String deleteIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_DELETECERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(deleteIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<TransactionReceipt> updateCertificateSource(String sourceIpfsHash, String certificateIpfsHash) {
        final Function function = new Function(
                FUNC_UPDATECERTIFICATESOURCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(certificateIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt updateCertificateSource(String sourceIpfsHash, String certificateIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_UPDATECERTIFICATESOURCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(certificateIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<String> getCertificate(String ipfsHash) {
        final Function function = new Function(FUNC_GETCERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createCertificate(String sourceIpfsHash, String newIpfsHash) {
        final Function function = new Function(
                FUNC_CREATECERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public TransactionReceipt createCertificate(String sourceIpfsHash, String newIpfsHash, Credentials credentials) {
        final Function function = new Function(
                FUNC_CREATECERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(sourceIpfsHash),
                        new org.web3j.abi.datatypes.Utf8String(newIpfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, credentials);
    }

    public RemoteFunctionCall<Boolean> checkCertificate(String ipfsHash) {
        final Function function = new Function(FUNC_CHECKCERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ipfsHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<LogCreateCertificateEventResponse> getLogCreateCertificateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGCREATECERTIFICATE_EVENT, transactionReceipt);
        ArrayList<LogCreateCertificateEventResponse> responses = new ArrayList<LogCreateCertificateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogCreateCertificateEventResponse typedResponse = new LogCreateCertificateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogCreateCertificateEventResponse> logCreateCertificateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogCreateCertificateEventResponse>() {
            @Override
            public LogCreateCertificateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGCREATECERTIFICATE_EVENT, log);
                LogCreateCertificateEventResponse typedResponse = new LogCreateCertificateEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogCreateCertificateEventResponse> logCreateCertificateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGCREATECERTIFICATE_EVENT));
        return logCreateCertificateEventFlowable(filter);
    }

    public List<LogUpdateCertificateEventResponse> getLogUpdateCertificateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGUPDATECERTIFICATE_EVENT, transactionReceipt);
        ArrayList<LogUpdateCertificateEventResponse> responses = new ArrayList<LogUpdateCertificateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogUpdateCertificateEventResponse typedResponse = new LogUpdateCertificateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogUpdateCertificateEventResponse> logUpdateCertificateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogUpdateCertificateEventResponse>() {
            @Override
            public LogUpdateCertificateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGUPDATECERTIFICATE_EVENT, log);
                LogUpdateCertificateEventResponse typedResponse = new LogUpdateCertificateEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogUpdateCertificateEventResponse> logUpdateCertificateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGUPDATECERTIFICATE_EVENT));
        return logUpdateCertificateEventFlowable(filter);
    }

    public List<LogDeleteCertificateEventResponse> getLogDeleteCertificateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGDELETECERTIFICATE_EVENT, transactionReceipt);
        ArrayList<LogDeleteCertificateEventResponse> responses = new ArrayList<LogDeleteCertificateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogDeleteCertificateEventResponse typedResponse = new LogDeleteCertificateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LogDeleteCertificateEventResponse> logDeleteCertificateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, LogDeleteCertificateEventResponse>() {
            @Override
            public LogDeleteCertificateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOGDELETECERTIFICATE_EVENT, log);
                LogDeleteCertificateEventResponse typedResponse = new LogDeleteCertificateEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.deleteIpfsHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LogDeleteCertificateEventResponse> logDeleteCertificateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGDELETECERTIFICATE_EVENT));
        return logDeleteCertificateEventFlowable(filter);
    }

    @Deprecated
    public static CertifierContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CertifierContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CertifierContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CertifierContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CertifierContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CertifierContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CertifierContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CertifierContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CertifierContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String accreditorContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, accreditorContractAddress)));
        return deployRemoteCall(CertifierContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<CertifierContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String accreditorContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, accreditorContractAddress)));
        return deployRemoteCall(CertifierContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CertifierContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String accreditorContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, accreditorContractAddress)));
        return deployRemoteCall(CertifierContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CertifierContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String accreditorContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, accreditorContractAddress)));
        return deployRemoteCall(CertifierContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class LogCreateCertificateEventResponse extends BaseEventResponse {
        public String sender;

        public String ipfsHash;
    }

    public static class LogUpdateCertificateEventResponse extends BaseEventResponse {
        public String sender;

        public String ipfsHash;
    }

    public static class LogDeleteCertificateEventResponse extends BaseEventResponse {
        public String sender;

        public String deleteIpfsHash;
    }
}
