package pe.com.nttdata.wsoperaciones.service;

import pe.com.nttdata.wsoperaciones.bean.Historico;
import pe.com.nttdata.wsoperaciones.bean.Product;
import pe.com.nttdata.wsoperaciones.request.ProductRequest;
import pe.com.nttdata.wsoperaciones.response.CustomerResponse;
import pe.com.nttdata.wsoperaciones.response.ResponseData;
import pe.com.nttdata.wsoperaciones.response.ResponseGeneric;
import reactor.core.publisher.Mono;

public interface OperacionServiceInf {
    public Mono<Product>getByIdProduct(String id);
    public Mono<ResponseData> consultaSaldo(String id);
    public Mono<CustomerResponse> consultarCliente(String id);

    public Mono<ResponseGeneric>operaciones(ProductRequest request);

    

}
