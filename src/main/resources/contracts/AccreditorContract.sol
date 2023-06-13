pragma solidity >=0.5.0 <=0.5.10;
pragma experimental ABIEncoderV2;

import "./StringSet.sol";

contract AccreditorContract{

    using StringSet for StringSet.Set;

    StringSet.Set accreditorSet;
    StringSet.Set certifierSet;

    struct AuthorityStruct {
        address personalAddress;
        string sourceIpfsHash;
    }

    mapping(string => AuthorityStruct) accreditors;
    mapping(string => AuthorityStruct) certifiers;

    event LogCreateAccreditor   (string sourceIpfsHash, address newAccreditor,  string ipfsHash);
    event LogCreateCertifier    (string sourceIpfsHash, address newCertifier,   string ipfsHash);

    event LogUpdateAccreditor   (string sourceIpfsHash, address editAccreditor, string oldIpfsHash, string newIpfsHash);
    event LogUpdateCertifier    (string sourceIpfsHash, address editCertifier,  string oldIpfsHash, string newIpfsHash);

    event LogDeleteAccreditor   (string sourceIpfsHash, address deleteAccreditor,   string deleteIpfsHash);
    event LogDeleteCertifier    (string sourceIpfsHash, address deleteCertifier,    string deleteIpfsHash);

    modifier onlyAccreditor(string memory ipfsHash) {

        require(getAccreditorAddress(ipfsHash)==msg.sender,
            "You don't have permission. (You are not on the list of Accreditors)");
        _;
    }

    //------------------------------------------------------------------------------------------------------------------

    constructor(string memory ipfsHash) public{
        //this constructor is like a createAccreditor
        require(keccak256(bytes(ipfsHash)) != keccak256(bytes("")),
            "Can't deploy a contract. Source IPFS hash is empty");

        accreditorSet.insert(ipfsHash);

        AuthorityStruct storage a = accreditors[ipfsHash];

        a.personalAddress = msg.sender;
        a.sourceIpfsHash = ipfsHash;

        emit LogCreateAccreditor(a.sourceIpfsHash, a.personalAddress, ipfsHash);
    }

    function createAccreditor(string memory sourceIpfsHash, address newAccreditor, string memory newIpfsHash)
    onlyAccreditor(sourceIpfsHash) public {
        require(keccak256(bytes(newIpfsHash)) != keccak256(bytes("")),
            "Can't create an accreditor. IPFS hash is empty");

        accreditorSet.insert(newIpfsHash);

        AuthorityStruct storage a = accreditors[newIpfsHash];

        a.personalAddress = newAccreditor;
        a.sourceIpfsHash = sourceIpfsHash;

        emit LogCreateAccreditor(a.sourceIpfsHash, a.personalAddress, newIpfsHash);
    }

    function updateAccreditor(string memory sourceIpfsHash, string memory oldIpfsHash, string memory newIpfsHash)
    onlyAccreditor(sourceIpfsHash)  public {

        require(accreditorSet.exists(oldIpfsHash),
            "Can't update an accreditor's IPFS hash. Old hash doesn't exist.");

        address personalAddress= deleteAccreditor(sourceIpfsHash, oldIpfsHash);

        createAccreditor(sourceIpfsHash, personalAddress, newIpfsHash);

        emit LogUpdateAccreditor(sourceIpfsHash, personalAddress, oldIpfsHash, newIpfsHash);
    }

    function deleteAccreditor(string memory sourceIpfsHash, string memory deleteIpfsHash)
    onlyAccreditor(sourceIpfsHash)  public returns(address){
        require(accreditorSet.keyList.length > 1, "Unable to delete. You are the last accreditor");

        accreditorSet.remove(deleteIpfsHash);

        address deletePersonalAddress = accreditors[deleteIpfsHash].personalAddress;

        delete accreditors[deleteIpfsHash];

        emit LogDeleteAccreditor(sourceIpfsHash, deletePersonalAddress, deleteIpfsHash);

        return deletePersonalAddress;

    }

    function getAccreditorAddress(string memory ipfsHash) public view returns(address) {
        require(accreditorSet.exists(ipfsHash), "Can't get a accreditor that doesn't exist.");

        return(accreditors[ipfsHash].personalAddress);
    }

    function getAccreditorSource(string memory ipfsHash) public view returns(string memory) {
        require(accreditorSet.exists(ipfsHash), "Can't get a accreditor that doesn't exist.");

        return(accreditors[ipfsHash].sourceIpfsHash);
    }

    function checkAccreditor(string memory ipfsHash) public view returns(bool) {

        return(accreditorSet.exists(ipfsHash));
    }

    function getAllAccreditors() public view returns(string[] memory accreditorList) {
        return accreditorSet.keyList;
    }

    //------------------------------------------------------------------------------------------------------------------

    function createCertifier(string memory sourceIpfsHash, address newCertifier, string memory newIpfsHash)
    onlyAccreditor(sourceIpfsHash) public {

        certifierSet.insert(newIpfsHash);

        AuthorityStruct storage a = certifiers[newIpfsHash];

        a.personalAddress = newCertifier;
        a.sourceIpfsHash = sourceIpfsHash;

        emit LogCreateCertifier(a.sourceIpfsHash, a.personalAddress, newIpfsHash);
    }

    function updateCertifier(string memory sourceIpfsHash, string memory oldIpfsHash, string memory newIpfsHash)
    onlyAccreditor(sourceIpfsHash)  public {

        require(certifierSet.exists(oldIpfsHash),
            "Can't update an certifier's IPFS hash. Old hash doesn't exist.");

        address personalAddress= deleteCertifier(sourceIpfsHash, oldIpfsHash);

        createCertifier(sourceIpfsHash, personalAddress, newIpfsHash);

        emit LogUpdateCertifier(sourceIpfsHash, personalAddress, oldIpfsHash, newIpfsHash);
    }

    function deleteCertifier(string memory sourceIpfsHash, string memory deleteIpfsHash)
    onlyAccreditor(sourceIpfsHash)  public returns(address){

        certifierSet.remove(deleteIpfsHash);

        address deletePersonalAddress = certifiers[deleteIpfsHash].personalAddress;

        delete certifiers[deleteIpfsHash];

        emit LogDeleteCertifier(sourceIpfsHash, deletePersonalAddress, deleteIpfsHash);

        return deletePersonalAddress;

    }

    function getCertifierAddress(string memory ipfsHash) public view returns(address) {
        require(certifierSet.exists(ipfsHash), "Can't get a certifier that doesn't exist.");

        return(certifiers[ipfsHash].personalAddress);
    }

    function getCertifierSource(string memory ipfsHash) public view returns(string memory) {
        require(certifierSet.exists(ipfsHash), "Can't get a certifier that doesn't exist.");

        return(certifiers[ipfsHash].sourceIpfsHash);
    }

    function checkCertifier(string memory ipfsHash) public view returns(bool) {

        return(certifierSet.exists(ipfsHash));
    }

    function getAllCertifiers() public view returns(string[] memory certifierList) {
        return certifierSet.keyList;
    }
}