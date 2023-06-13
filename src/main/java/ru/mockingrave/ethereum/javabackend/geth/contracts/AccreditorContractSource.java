package ru.mockingrave.ethereum.javabackend.geth.contracts;

import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

public abstract class AccreditorContractSource extends Contract {

    public AccreditorContractSource(String contractBinary,
                                       String contractAddress,
                                       Web3j web3j,
                                       TransactionManager transactionManager,
                                       ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);

    }

}
