package com.ndiamond.paintshop.service.cart;

import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Cart;
import com.ndiamond.paintshop.model.CartItem;
import com.ndiamond.paintshop.model.Product;
import com.ndiamond.paintshop.repository.CartItemRepository;
import com.ndiamond.paintshop.repository.CartRepository;
import com.ndiamond.paintshop.request.CartItemRequest;
import com.ndiamond.paintshop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

//    public CartItemService(CartItemRepository cartItemRepository, IProductService productService) {
//        this.cartItemRepository = cartItemRepository;
//        this.productService = productService;
//    }



    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. Get the product
        //3. Check if the product already in the cart
        //4. If yes, then increase the quantity with the requested quantity
        //5. If No, then initiate a new CartItem entry
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if (cartItem.getId()==null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void addItemsToCart(Long cartId, List<CartItemRequest> items) {

        // 1. Get cart
        Cart cart = cartService.getCart(cartId);

        // 2. Prepare productIds list
        List<Long> productIds = items.stream()
                .map(CartItemRequest::getProductId)
                .toList();

        // 3. Fetch all products in ONE query
        Map<Long, Product> productMap = productService.getProductsByIds(productIds);

        // 4. Convert existing cart items to Map (O(1) lookup)
        Map<Long, CartItem> cartItemMap = cart.getItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        item -> item
                ));

        // 5. Process items
        List<CartItem> updatedItems = items.stream().map(request -> {

            Long productId = request.getProductId();
            int quantity = request.getQuantity();

            // 6. Get product from preloaded map
            Product product = productMap.get(productId);
            if (product == null) {
                throw new ResourceNotFoundException("Product not found with id: " + productId);
            }

            // 7. Get or create cart item
            CartItem cartItem = cartItemMap.getOrDefault(productId, new CartItem());

            if (cartItem.getId() == null) {
                // New item
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(product.getPrice());

                // Important: update map for duplicate handling
                cartItemMap.put(productId, cartItem);

            } else {
                // Existing item
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }

            // 8. Update total price
            cartItem.setTotalPrice();

            // 9. Add to cart (ensure no duplicates depending on implementation)
            cart.addItem(cartItem);

            return cartItem;

        }).toList();

        // 10. Save all items at once
        cartItemRepository.saveAll(updatedItems);

        // 11. Save cart
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Transactional
    @Override
    public void decreaseItemQuantity(Long userId, Long productId) {

        // 🔹 Get cart
        Cart cart = cartService.getCartByUserId(userId);

        // 🔹 Find cart item
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // 🔥 Logic
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            // quantity == 1 → remove item
            cart.getItems().remove(item); // 🔥 remove from parent

            cartItemRepository.delete(item);
        }
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }
}
