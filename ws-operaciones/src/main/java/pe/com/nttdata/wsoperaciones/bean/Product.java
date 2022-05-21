package pe.com.nttdata.wsoperaciones.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private double commission;
    private int numberOfMovements;
    private int numberOfCredit;
    private double amount;
    private int limitCredit;
    private String action;
    private String idTypeProduct;
    private String idCustomer;

}