package pe.com.nttdata.wsoperaciones.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String id;
    private Double amount;
    private String action;
}
