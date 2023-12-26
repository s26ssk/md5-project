package com.ra.controller;

import com.ra.dto.request.AddressRequest;
import com.ra.dto.request.UpdateUserRequest;
import com.ra.dto.response.AddressResponse;
import com.ra.model.Address;
import com.ra.model.Category;
import com.ra.model.Product;
import com.ra.model.Users;
import com.ra.service.ICategoryService;
import com.ra.service.IProductService;
import com.ra.service.IUserService;
import com.ra.util.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;
    @GetMapping("/account")
    public ResponseEntity<Users> getUserAccount() throws AppException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println(username);

        Users user = userService.getUserByUsername(username)
                .orElseThrow(() -> new AppException("User not found"));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/account/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword,
                                                 @RequestParam String newPassword) throws AppException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!userService.checkPassword(username, oldPassword)) {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        userService.changePassword(username, newPassword);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }
    @PutMapping("/account")
    public ResponseEntity<String> updateCurrentUser(@RequestBody UpdateUserRequest updateUserRequest) throws AppException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new AppException("User not found"));

        currentUser.setFullName(updateUserRequest.getFullName());

        userService.updateUser(currentUser);

        return new ResponseEntity<>("User information updated successfully", HttpStatus.OK);
    }
    @GetMapping("/account/address")
    public ResponseEntity<List<AddressResponse>> getUserAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            List<AddressResponse> addresses = userService.getUserAddresses(username);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/account/address/{addressId}")
    public ResponseEntity<AddressResponse> getUserAddressById(@PathVariable Long addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            AddressResponse addressResponse = userService.getUserAddressById(username, addressId);
            return new ResponseEntity<>(addressResponse, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/account/address")
    public ResponseEntity<String> addAddress(@RequestBody AddressRequest addressRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.addAddress(username, addressRequest);

        return new ResponseEntity<>("Address added successfully", HttpStatus.OK);
    }
    @DeleteMapping("/account/address/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            userService.deleteAddress(username, addressId);
            return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Category
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Product
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @PageableDefault(page = 0, size = 2)
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = "productId") String sortBy) {

        Pageable pageable = PageRequest.of(page, 2, Sort.by(sortBy));
        Page<Product> productPage = productService.getAllProducts(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("product", productPage.getContent());
        data.put("sizePage", productPage.getSize());
        data.put("totalElement", productPage.getTotalElements());
        data.put("totalPages", productPage.getTotalPages());
        data.put("currentPage", productPage.getNumber());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<Map<String, Object>> searchProductsByName(
            @RequestParam(name = "keyword") String productName,
            @PageableDefault(page = 0, size = 2) Pageable pageable) {

        Page<Product> productsPage;

        if (productName != null && !productName.isEmpty()) {
            productsPage = productService.searchProductsByName(productName, pageable);
        } else {
            productsPage = productService.getAllProducts(pageable);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("products", productsPage.getContent());
        data.put("sizePage", productsPage.getSize());
        data.put("totalElement", productsPage.getTotalElements());
        data.put("totalPages", productsPage.getTotalPages());
        data.put("currentPage", productsPage.getNumber());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Optional<Product> productOptional = productService.getProductById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products/new-products")
    public ResponseEntity<List<Product>> getLatestProducts(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<Product> latestProducts = productService.getLatestProducts(pageable);
        List<Product> products = latestProducts.getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @PageableDefault(page = 0, size = 2) Pageable pageable) {
        Page<Product> products = productService.getProductsByCategoryId(categoryId, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
