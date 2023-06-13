package ru.mockingrave.ethereum.javabackend.geth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import ru.mockingrave.ethereum.javabackend.geth.dto.EthAccountDto;
import ru.mockingrave.ethereum.javabackend.geth.dto.InfoDto;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Optional;

@Service
public class GethService {

    @Value("${geth.keystore.path}")
    protected String KEY_PATH;

    @Autowired
    protected Web3j web3j;

    @PostConstruct
    public InfoDto connectionTest() {

        var response = new HashMap<String, String>();
        try {
            // web3_clientVersion returns the current client version.
            var clientVersion = web3j.web3ClientVersion().send();
            response.put("Client version", clientVersion.getWeb3ClientVersion());

            //eth_blockNumber returns the number of most recent block.
            var blockNumber = web3j.ethBlockNumber().send();
            response.put("Block number", blockNumber.getBlockNumber().toString());

            //eth_gasPrice, returns the current price per gas in wei.
            var gasPrice = web3j.ethGasPrice().send();
            response.put("Gas price", gasPrice.getGasPrice() + " eth");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InfoDto(response);
    }

    public String walletToAccount(String walletName){
        return walletName.substring(38, 38+41);
    }
    public EthAccountDto createNewAccount(String password) {
        var response = new EthAccountDto();

        try {
            String walletName = WalletUtils.generateFullNewWalletFile(password, new File(KEY_PATH));
            response.setWallet(walletName);

            Credentials credentials = WalletUtils.loadCredentials(password, KEY_PATH + walletName);
            response.setAddress(credentials.getAddress());

        } catch (NoSuchAlgorithmException | IOException | CipherException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return response;
    }

    public EthAccountDto checkAccount(String walletName, String password) {
        var response = new EthAccountDto();

        try {
            String source = KEY_PATH + walletName;
            Credentials credentials = WalletUtils.loadCredentials(password, source);

            response.setWallet(walletName);
            response.setAddress(credentials.getAddress());
            response.setBalance(getBalance(credentials.getAddress()));

        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return response;
    }

    public InfoDto transferMoney(String walletName, String password, String addressTo, String value, String gasLimit, String gasPrice) {
        var response = new HashMap<String, String>();

        try {
            var source = KEY_PATH + walletName;
            var credentials = WalletUtils.loadCredentials(password, source);

            response.put("Balance 'From' (before)", getBalance(credentials.getAddress()));
            response.put("Balance 'To' (before)", getBalance(addressTo));

            BigInteger bIntValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
            BigInteger bIntGasLimit = BigInteger.valueOf(Long.parseLong(gasLimit));
            BigInteger bIntGasPrice = Convert.toWei(gasPrice, Convert.Unit.ETHER).toBigInteger();

            var rawTransactionManager = new RawTransactionManager(web3j, credentials, Long.parseLong(web3j.netVersion().send().getNetVersion()));

            var ethSendTransaction = rawTransactionManager.sendTransaction(bIntGasPrice, bIntGasLimit, addressTo, "", bIntValue);

            var transactionHash = ethSendTransaction.getTransactionHash();
            response.put("transactionHash", transactionHash);
            //Wait for transaction to be mined
            Optional<TransactionReceipt> transactionReceipt = null;
            do {
                System.out.println("checking if transaction " + transactionHash + " is mined...");
                EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(transactionHash).send();
                transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
                try {
                    Thread.sleep(500); // Wait 1 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!transactionReceipt.isPresent());

            response.put("Balance 'From' (after)", getBalance(credentials.getAddress()));
            response.put("Balance 'To' (after)", getBalance(addressTo));

            response.put("address FROM", credentials.getAddress());
            response.put("address TO", addressTo);

        } catch (IOException | CipherException e) {
            e.printStackTrace();
            response.put("Error", "Invalid name or password.");
        }
        return new InfoDto(response);
    }

    private String getBalance(String address) {
        try {
            return Convert.fromWei
                    (web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                            .send().getBalance().toString(), Convert.Unit.ETHER)
                    .toString() + " Eth";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
