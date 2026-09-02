package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.order.AddOrderRequest;
import com.danny.shoppingplatform.dto.order.OrderResponse;
import com.danny.shoppingplatform.model.*;
import com.danny.shoppingplatform.repository.*;
import com.danny.shoppingplatform.util.NumberUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final VendorRepository vendorRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse addOrder(AddOrderRequest request, String account) {
        Member member = memberRepository.findByUserAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Member with account '%s' not found".formatted(account)));

        Long vendorId = request.getVendorId();
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException("Vendor with ID '%s' not found".formatted(vendorId)));

        List<Long> cartIds = request.getCartIds();
        Long memberId = member.getId();
        List<Cart> selectedCarts = cartRepository.findByIdInAndMemberId(cartIds, memberId);

        // 驗證查詢出來的筆數是否跟前端傳入的 ID 數量相符
        if (selectedCarts.isEmpty()) {
            throw new IllegalStateException("未找到對應的購物車商品項目");
        }
        if (selectedCarts.size() != cartIds.size()) {
            throw new IllegalArgumentException("包含無效或不屬於該會員的購物車項目");
        }

        Order order = new Order();
        order.setMember(member);
        order.setVendor(vendor);
        order.setOrderNumber(NumberUtil.generateOrderNumber());
        order.setStatus("PENDING"); // 初始狀態：待付款 / 處理中
        order.setCreatedDate(Instant.now());
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverEmail(request.getReceiverEmail());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNote(request.getNote());

        BigDecimal shippingFee = request.getShippingFee() != null ? request.getShippingFee() : BigDecimal.ZERO;
        order.setShippingFee(shippingFee);

        BigDecimal itemSubtotal = BigDecimal.ZERO;

        for (Cart cart : selectedCarts) {
            Product product = cart.getProduct();

            if (product.getQuantity() < cart.getQuantity()) {
                String errMsg = "商品 [" + product.getName() + "] 庫存不足！當前庫存：" + product.getQuantity() + "，購買數量：" + cart.getQuantity();
                throw new IllegalStateException(errMsg);
            }

            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productRepository.save(product); // 可移除

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());

            // 因設定 CascadeType.ALL, order 儲存時會一併儲存
            order.addOrderItem(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            itemSubtotal = itemSubtotal.add(itemTotal);
        }

        order.setTotalAmount(itemSubtotal.add(shippingFee));

        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteByIdInAndMemberId(cartIds, memberId);

        return OrderResponse.fromEntity(savedOrder);
    }
}
