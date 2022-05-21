package pe.com.nttdata.wsoperaciones.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pe.com.nttdata.wsoperaciones.bean.*;
import pe.com.nttdata.wsoperaciones.client.OperacionClient;
import pe.com.nttdata.wsoperaciones.request.ProductRequest;
import pe.com.nttdata.wsoperaciones.response.ConsultaCuenta;
import pe.com.nttdata.wsoperaciones.response.CustomerResponse;
import pe.com.nttdata.wsoperaciones.response.ResponseData;
import pe.com.nttdata.wsoperaciones.response.ResponseGeneric;
import reactor.core.publisher.Mono;


import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
//@CacheConfig(cacheNames = "product")
public class OperacionServiceImpl implements OperacionServiceInf{

    OperacionClient operacionClient=new OperacionClient();

    @Override
    //@Cacheable (cacheNames = "product" , key = "#id")
    public Mono<Product>getByIdProduct(String id){
        log.info("==========================> {}",id);
        return this.operacionClient.findByIdProduct(id);
    }
    //@Cacheable(value = "operaciones", key = "#id")
    @Override
    public Mono<ResponseData>consultaSaldo(String id){
        ResponseData response=new ResponseData();
        ConsultaCuenta consultaCuenta=new ConsultaCuenta();
        ResponseGeneric responseGeneric=new ResponseGeneric();
        return this.operacionClient.findByIdProduct(id).defaultIfEmpty(new Product())
                .flatMap(p->{
                    if (p.getId()!=null){

                        return this.operacionClient.findByIdCustomer(p.getIdCustomer())
                                .switchIfEmpty(Mono.error(new InterruptedException("Accounts not found")))
                                .flatMap(c->{
                                   if (c.getId()!=null){
                                        return this.operacionClient.findAllTypeProduct().defaultIfEmpty(new TypeProduct())
                                                        .collectList()
                                                        .log()
                                                        .flatMap(t->{
                                                            for (TypeProduct typeProduct:t) {
                                                                if (typeProduct.getId().equals(p.getIdTypeProduct())){
                                                                    consultaCuenta.setNombreUsuario(c.getName());
                                                                    consultaCuenta.setAccount(typeProduct.getAccount());
                                                                    consultaCuenta.setType(typeProduct.getType());
                                                                    consultaCuenta.setFechaConsulta(LocalDateTime.now());
                                                                    consultaCuenta.setSaldoTotal(p.getAmount());
                                                                    responseGeneric.setCod("0");
                                                                    responseGeneric.setMsg("ok");
                                                                    response.setData(consultaCuenta);
                                                                    response.setResponseGeneric(responseGeneric);
                                                                    return Mono.just(response);
                                                                }
                                                            }

                                                            responseGeneric.setCod("-0");
                                                            responseGeneric.setMsg("los ids no son iguales");
                                                            response.setData(null);
                                                            response.setResponseGeneric(responseGeneric);
                                                            return Mono.just(response);
                                                        });

                                   }
                                    responseGeneric.setCod("-0");
                                    responseGeneric.setMsg("id de cliente no existe");
                                    response.setData(null);
                                    response.setResponseGeneric(responseGeneric);
                                    return Mono.just(response);
                                });
                    }
                    responseGeneric.setCod("-0");
                    responseGeneric.setMsg("Id de producto no existe");
                    response.setData(null);
                    response.setResponseGeneric(responseGeneric);
                    return Mono.just(response);
                });
    }

    @Override
    public Mono<CustomerResponse> consultarCliente(String id) {
        CustomerResponse response = new CustomerResponse();
        ResponseGeneric generic=new ResponseGeneric();

        return this.operacionClient.findByIdCustomer(id).defaultIfEmpty(new Customer())
                .flatMap(customer -> {
                    if (customer.getId()!=null){
                        return this.operacionClient.findByIdTypeCustomer(customer.getId()).defaultIfEmpty(new TypeCustomer())
                                .collectList()
                                .flatMap(typeCustomers -> {
                                    return this.operacionClient.findByIdCustomers(customer.getId()).defaultIfEmpty(new Product())
                                                    .collectList()
                                                            .flatMap(products -> {
                                                                customer.setTypeCustomers(typeCustomers);
                                                                customer.setProducts(products);
                                                                response.setCustomer(customer);
                                                                generic.setCod("0");
                                                                generic.setMsg("Consulta exitoso");
                                                                response.setEstado(generic);
                                                                return Mono.just(response);
                                                            });

                                });
                    }
                    generic.setCod("-1");
                    generic.setMsg("No existe el id cliente");
                    response.setEstado(generic);
                    response.setCustomer(null);
                    response.setEstado(generic);
                    return Mono.just(response);
                });
    }

    @Override
    public Mono<ResponseGeneric> operaciones(ProductRequest request) {
        ResponseGeneric response=new ResponseGeneric();
        Historico historico=new Historico();
        Double resp=0.0;
        return this.operacionClient.findAllTypeProduct().collectList()
                .flatMap(typeProducts -> {
                    for (TypeProduct typeProduct:typeProducts) {
                        if (typeProduct.isStatus()){
                            return this.operacionClient.findByIdProduct(request.getId())
                                    .flatMap(product -> {
                                        if (product!=null){
                                            product.setNumberOfMovements(product.getNumberOfMovements()+1);
                                            product.setAction(request.getAction());
                                            if (request.getAction().equals("deposito")){
                                                product.setAmount(product.getAmount()+request.getAmount());

                                            }else if(request.getAction().equalsIgnoreCase("retiro")){
                                                if (product.getAmount()>0 && product.getAmount()>=request.getAmount()) {
                                                    product.setAmount(product.getAmount() - request.getAmount());

                                                }else {
                                                    response.setCod("-1");
                                                    response.setMsg("Salda insuficiente");
                                                    return Mono.just(response);
                                                }
                                            }else if(request.getAction().equalsIgnoreCase("pago")) {
                                                if (product.getAmount()>0 && product.getAmount()>=request.getAmount()) {
                                                    product.setAmount(product.getAmount() - request.getAmount());

                                                }else {
                                                    response.setCod("-1");
                                                    response.setMsg("Salda insuficiente para pagar");
                                                    return Mono.just(response);
                                                }
                                            }
                                            return this.operacionClient.saveProduct(product)
                                                    .flatMap(product1 -> {
                                                        LocalDateTime currentDateTime = LocalDateTime.now();
                                                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                                                        String formattedDateTime = currentDateTime.format(formatter);
                                                        historico.setMontoActual(product.getAmount());
                                                        historico.setIdOpreracion(product.getId());
                                                        historico.setCommission(0);
                                                        historico.setNumberOfMovements(product.getNumberOfMovements());
                                                        historico.setNumberOfCredit(0);
                                                        historico.setLimitCredit(0);
                                                        historico.setAction(product.getAction());
                                                        historico.setIdTypeProduct(product.getIdTypeProduct());
                                                        historico.setIdCustomer(product.getIdCustomer());
                                                        historico.setFechaOperacion(formattedDateTime);
                                                        historico.setDevice("Inet4Address.getLocalHost().getHostName()");
                                                        return this.operacionClient.saveHistorico(historico).flatMap(h->{
                                                            response.setCod("0");
                                                            response.setMsg("ok");
                                                            return Mono.just(response);
                                                        });

                                                    });

                                        }
                                        response.setCod("-1");
                                        response.setMsg("Producto vacio");
                                        return Mono.just(response);
                                    });
                        }
                    }
                    response.setCod("-1");
                    response.setMsg("Estado falso");
                    return Mono.just(response);
                });

    }






}
