package ru.mockingrave.ethereum.javabackend.elasticsearch.model.substruct;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorityDataPack {

    String name;
    String ipfsHash;
    String ethAddress;
}
