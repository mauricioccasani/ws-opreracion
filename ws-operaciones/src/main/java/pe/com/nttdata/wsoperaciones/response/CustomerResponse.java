package pe.com.nttdata.wsoperaciones.response;

import lombok.Data;
import pe.com.nttdata.wsoperaciones.bean.Customer;
@Data
public class CustomerResponse {
    private Customer customer;
    private ResponseGeneric estado;
}
