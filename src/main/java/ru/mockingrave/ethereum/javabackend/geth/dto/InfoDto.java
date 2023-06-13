package ru.mockingrave.ethereum.javabackend.geth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {

    Map<String, String> information;
}
