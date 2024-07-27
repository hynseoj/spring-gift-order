package gift.domain.product.service;

import gift.domain.product.dto.OptionRequest;
import gift.domain.product.dto.OptionResponse;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.OptionJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.exception.InvalidOptionInfoException;
import gift.exception.InvalidProductInfoException;
import java.util.List;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OptionManager {

    private final OptionJpaRepository optionJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public OptionManager(OptionJpaRepository optionJpaRepository, ProductJpaRepository productJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
        this.productJpaRepository = productJpaRepository;

    }

    public void create(Product product, List<OptionRequest> optionRequestDtos) {
        for (OptionRequest optionRequest : optionRequestDtos) {
            Option option = optionRequest.toOption(product);
            product.addOption(option);
        }
    }

    public List<OptionResponse> readAll(long productId) {
        Product product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));
        List<Option> options = product.getOptions();
        return options.stream().map(OptionResponse::from).toList();
    }

    @Transactional
    public void update(Product product, List<OptionRequest> optionRequestDtos) {
        optionJpaRepository.deleteAllByProductId(product.getId());
        product.removeOptions();
        create(product, optionRequestDtos);
    }

    @Transactional
    public void deleteAllByProductId(long productId) {
        optionJpaRepository.deleteAllByProductId(productId);
    }

    @Transactional
    @Retryable(
        retryFor = {ObjectOptimisticLockingFailureException.class},
        maxAttempts = 100,
        backoff = @Backoff(delay = 100)
    )
    public Option subtractQuantity(long optionId, int quantity) {
        Option option = optionJpaRepository.findByIdWithOptimisticLock(optionId)
            .orElseThrow(() -> new InvalidOptionInfoException("error.invalid.option.id"));
        option.subtract(quantity);
        return option;
    }
}