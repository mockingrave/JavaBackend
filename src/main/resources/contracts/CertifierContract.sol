pragma solidity >=0.5.0 <=0.5.10;

contract CertifierContract{

    AccreditorContract ac;
    mapping(string => string) certificates;

    modifier onlyCertifier(string memory ipfsHash) {

        require(
            ac.getCertifierAddress(ipfsHash)==msg.sender,
            "You don't have permission. (You are not on the list of Certifiers)");
        _;
    }

    constructor(address accreditorContractAddress) public{
        ac = AccreditorContract(accreditorContractAddress);
    }

    event LogCreateCertificate(string sender, string ipfsHash);
    event LogUpdateCertificate(string sender, string ipfsHash);
    event LogDeleteCertificate(string sender, string deleteIpfsHash);

    function createCertificate(string memory sourceIpfsHash, string memory newIpfsHash)
    onlyCertifier(sourceIpfsHash) public {

        require(keccak256(bytes(newIpfsHash)) != keccak256(bytes("")),
            "Can't create a certificate. IPFS hash is empty");

        certificates[newIpfsHash] = sourceIpfsHash;

        emit LogCreateCertificate(sourceIpfsHash, newIpfsHash);
    }

    function updateCertificateSource(string memory sourceIpfsHash, string memory certificateIpfsHash)
    onlyCertifier(sourceIpfsHash)  public {

        certificates[certificateIpfsHash] = sourceIpfsHash;

        emit LogUpdateCertificate(sourceIpfsHash, certificateIpfsHash);
    }

    function deleteCertificate(string memory sourceIpfsHash, string memory deleteIpfsHash)
    onlyCertifier(sourceIpfsHash)  public {

        delete certificates[deleteIpfsHash];

        emit LogDeleteCertificate(sourceIpfsHash, deleteIpfsHash);
    }

    function getCertificate(string memory ipfsHash) public view returns(string memory certifier) {
        require(checkCertificate(ipfsHash), "Can't get a certificate that doesn't exist.");

        return(certificates[ipfsHash]);
    }

    function checkCertificate(string memory ipfsHash) public view returns(bool) {
        return (keccak256(bytes(certificates[ipfsHash])) != keccak256(bytes("")));
    }

}

contract AccreditorContract{

    function getCertifierAddress(string memory ipfsHash) public view returns(address);

    function checkAccreditor(string memory ipfsHash) public view returns(bool);

    function checkCertifier(string memory ipfsHash) public view returns(bool);

}