package gift.domain.order.service;

import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.entity.Order;
import gift.domain.order.entity.OrderItem;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.service.OptionManager;
import gift.domain.user.entity.User;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.exception.InvalidOptionInfoException;
import gift.exception.InvalidProductInfoException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

@Component
public class OrderItemManager {

    private final ProductJpaRepository productJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;
    private final OptionManager optionManager;

    public OrderItemManager(
        ProductJpaRepository productJpaRepository,
        WishlistJpaRepository wishlistJpaRepository,
        OptionManager optionManager
    ) {
        this.productJpaRepository = productJpaRepository;
        this.wishlistJpaRepository = wishlistJpaRepository;
        this.optionManager = optionManager;
    }

    public void create(User user, Order order, List<OrderItemRequest> orderItemRequests) {
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            Entry<Product, Option> item = buy(
                orderItemRequest.productId(), orderItemRequest.optionId(), orderItemRequest.quantity()
            );
            Product product = item.getKey();
            Option option = item.getValue();

            OrderItem orderItem = orderItemRequest.toOrderItem(order, product, option);
            order.addOrderItem(orderItem);

            wishlistJpaRepository.deleteByUserAndProduct(user, product);
        }
    }

    private Entry<Product, Option> buy(Long productId, Long optionId, int quantity) {
        Product product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        if (!product.hasOption(optionId)) {
            throw new InvalidOptionInfoException("error.invalid.option.id");
        }
        Option option = optionManager.subtractQuantity(optionId, quantity);

        return new SimpleEntry<>(product, option);
    }
}