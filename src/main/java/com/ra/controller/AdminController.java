package com.ra.controller;

import com.ra.model.Category;
import com.ra.model.Roles;
import com.ra.model.Users;
import com.ra.service.ICategoryService;
import com.ra.service.IRoleService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        List<Roles> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @PageableDefault(page = 0, size = 2)
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = "userId") String sortBy) {

        Pageable pageable = PageRequest.of(page, 2, Sort.by(sortBy));
        Page<Users> usersPage = userService.getAllUsers(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("users", usersPage.getContent());
        data.put("sizePage", usersPage.getSize());
        data.put("totalElement", usersPage.getTotalElements());
        data.put("totalPages", usersPage.getTotalPages());
        data.put("currentPage", usersPage.getNumber());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<Users> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<Map<String, Object>> getUsersByUsername(
            @RequestParam(name = "keyword", required = false) String username,
            @PageableDefault(page = 0, size = 2) Pageable pageable) {
        Page<Users> usersPage;

        if (username != null && !username.isEmpty()) {
            usersPage = userService.searchUsersByUsername(username, pageable);
        } else {
            usersPage = userService.getAllUsers(pageable);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("users", usersPage.getContent());
        data.put("sizePage", usersPage.getSize());
        data.put("totalElement", usersPage.getTotalElements());
        data.put("totalPages", usersPage.getTotalPages());
        data.put("currentPage", usersPage.getNumber());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/change-userStatus/{userId}")
    public ResponseEntity<String> toggleUserStatus(@PathVariable Long userId) throws AppException {
        boolean isBlocked = userService.toggleUserStatus(userId);
        if (isBlocked) {
            return new ResponseEntity<>("User blocked successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User unblocked successfully", HttpStatus.OK);
        }
    }

    @PostMapping("/change-password")
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


    // Category
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories(
            @PageableDefault(page = 0, size = 2)
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = "categoryId") String sortBy) {

        Pageable pageable = PageRequest.of(page, 2, Sort.by(sortBy));
        Page<Category> categoryPage = categoryService.getAllCategories(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categoryPage.getContent());
        data.put("sizePage", categoryPage.getSize());
        data.put("totalElement", categoryPage.getTotalElements());
        data.put("totalPages", categoryPage.getTotalPages());
        data.put("currentPage", categoryPage.getNumber());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> categoryOptional = categoryService.getCategoryById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestBody Category newCategory) throws AppException{
        categoryService.addCategory(newCategory);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category updatedCategory) {
        boolean isUpdated = categoryService.updateCategory(categoryId, updatedCategory);

        if (isUpdated) {
            return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category not found or update failed", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        boolean isDeleted = categoryService.deleteCategory(categoryId);

        if (isDeleted) {
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category not found or delete failed", HttpStatus.NOT_FOUND);
        }
    }

}
