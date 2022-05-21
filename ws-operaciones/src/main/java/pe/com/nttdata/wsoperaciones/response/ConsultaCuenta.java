package pe.com.nttdata.wsoperaciones.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaCuenta {
    private Double saldoTotal;
    private String nombreUsuario;
    private String type;
    private String account ;
    private LocalDateTime fechaConsulta;
}
