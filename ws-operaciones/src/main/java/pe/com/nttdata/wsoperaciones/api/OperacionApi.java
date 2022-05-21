package pe.com.nttdata.wsoperaciones.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import pe.com.nttdata.wsoperaciones.bean.Product;
import pe.com.nttdata.wsoperaciones.request.ProductRequest;
import pe.com.nttdata.wsoperaciones.response.CustomerResponse;
import pe.com.nttdata.wsoperaciones.response.ResponseData;
import pe.com.nttdata.wsoperaciones.response.ResponseGeneric;
import pe.com.nttdata.wsoperaciones.service.OperacionServiceImpl;
import pe.com.nttdata.wsoperaciones.service.OperacionServiceInf;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class OperacionApi {
    @Autowired
    private OperacionServiceInf operacionServiceImpl;


    @GetMapping("/product/{id}")
    public Mono<Product>getByIdProduct2(@PathVariable String id){
        return this.operacionServiceImpl.getByIdProduct(id);
    }
    @GetMapping("/{id}")
    public Mono<ResponseData>getByIdProduct(@PathVariable String id){
        return this.operacionServiceImpl.consultaSaldo(id);
    }

    @GetMapping("/customer/{id}")
    public Mono<CustomerResponse>getCustomer(@PathVariable String id){
        return this.operacionServiceImpl.consultarCliente(id);
    }
    @PostMapping("/operacion")
    public Mono<ResponseGeneric> operations(@RequestBody ProductRequest product){
        log.info("===================> {}",product);
        return this.operacionServiceImpl.operaciones(product);
    }
}
