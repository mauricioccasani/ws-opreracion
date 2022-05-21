package pe.com.nttdata.wsoperaciones.bean;

import lombok.Data;

@Data
public class Historico {
    private String id;
    private double montoActual;
    private String idOpreracion;
    private double commission;
    private int numberOfMovements;
    //private double paymentAmount;
    private int numberOfCredit;
    private int limitCredit;
    private String action;
    private String idTypeProduct;
    private String idCustomer;

    private String fechaOperacion;
    private String device;
}
