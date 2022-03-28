package si.codemonkee.javersDemo.controller;

import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import si.codemonkee.javersDemo.dto.RebrandStoreDTO;
import si.codemonkee.javersDemo.dto.UpdatePriceDTO;
import si.codemonkee.javersDemo.model.Product;
import si.codemonkee.javersDemo.model.Store;
import si.codemonkee.javersDemo.service.StoreService;

import java.util.List;

@RestController
public class StoreController {
    private final StoreService storeService;
    private final Javers javers;

    public StoreController(StoreService customerService, Javers javers) {
        this.storeService = customerService;
        this.javers = javers;
    }

    @PostMapping("/stores/{storeId}/products/random")
    public void createRandomProduct(@PathVariable final Integer storeId) {
        storeService.createRandomProduct(storeId);
    }

    @PostMapping("/stores/{storeId}/rebrand")
    public void rebrandStore(@PathVariable final Integer storeId, @RequestBody RebrandStoreDTO rebrandStoreDto) {
        storeService.rebrandStore(storeId, rebrandStoreDto.name);
    }

    @PostMapping(value = "/stores/{storeId}/products/{productId}/price", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateProductPrice(@PathVariable final Integer productId, @PathVariable String storeId, @RequestBody UpdatePriceDTO priceDto) {
        storeService.updateProductPrice(productId, priceDto.price);
    }

    @RequestMapping(value = "/products/{productId}/changes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProductChanges(@PathVariable int productId) {
        Product product = storeService.findProductById(productId);
        QueryBuilder jqlQuery = QueryBuilder.byInstance(product);
        Changes changes = javers.findChanges(jqlQuery.build());
        return javers.getJsonConverter().toJson(changes);
    }

    @RequestMapping(value = "/products/snapshots", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProductSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Product.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

    @RequestMapping(value = "/stores/{storeId}/shadows", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStoreShadows(@PathVariable int storeId) {
        Store store = storeService.findStoreById(storeId);
        JqlQuery jqlQuery = QueryBuilder.byInstance(store)
                .withChildValueObjects().build();
        List<Shadow<Store>> shadows = javers.findShadows(jqlQuery);
        return javers.getJsonConverter().toJson(shadows.get(0));
    }

    @RequestMapping(value = "/stores/snapshots", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStoresSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Store.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

}
