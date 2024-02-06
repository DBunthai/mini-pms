package com.mini.pms.service.impl;

import com.mini.pms.customexception.PlatformException;
import com.mini.pms.entity.Property;
import com.mini.pms.repo.PropertyRepo;
import com.mini.pms.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Where(clause = "status != 'DELETED'")
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepo propertyRepo;

    @Override
    public Page<Property> findAll(
            String search,
            Double minPrice,
            Double maxPrice,
            String category,
            String type,
            String numberOfRoom,
            String location,
            Pageable pageable,
            Principal principal
    ) {

        var email = principal.getName();

        Specification<Property> spec = Specification.where(null);

        if (Objects.nonNull(minPrice)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)
            );
        }

        if (Objects.nonNull(maxPrice)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)
            );
        }

        if (Objects.nonNull(category)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category"), category));
        }

        if (Objects.nonNull(type)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (Objects.nonNull(numberOfRoom)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("numberOfRoom"), numberOfRoom));
        }

        if (Objects.nonNull(location)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("location"), location));
        }

        return propertyRepo.findAll(spec, pageable);
    }

    @Override
    public Property findById(long id) {
        return propertyRepo
                .findById(id)
                .orElseThrow(
                        () -> new PlatformException("Property not found", HttpStatus.NOT_FOUND));
    }
}
