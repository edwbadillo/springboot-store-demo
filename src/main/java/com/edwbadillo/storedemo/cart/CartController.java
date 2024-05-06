package com.edwbadillo.storedemo.cart;

import com.edwbadillo.storedemo.cart.dto.CustomerCartDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart", description = "The customer's shopping cart APIs")
@RestController
@PreAuthorize("hasRole('CUSTOMER')")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Get cart", description = "Get the authenticated customer's shopping cart.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer's cart" ,
                    content = { @Content(schema = @Schema(implementation = CustomerCartDetails.class), mediaType = "application/json") }),
    })
    @GetMapping
    public CustomerCartDetails getCart() {
        return cartService.getCart();
    }
}
