package ar.utn.frba.dds.modulos.datadog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDTO {
	
	private String src;
	private String dst;
	private Double amount;

}
