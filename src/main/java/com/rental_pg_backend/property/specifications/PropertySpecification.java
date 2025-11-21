package com.rental_pg_backend.property.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.rental_pg_backend.common.dto.NumericRangesDto;
import com.rental_pg_backend.property.dto.property.PropertySearchDto;
import com.rental_pg_backend.property.entities.Property;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PropertySpecification {

    private PropertySpecification() {
    }

    private static final String LOCATION = "location";

    public static Specification<Property> withDynamicQuery(
            PropertySearchDto propertySearchDto) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addLocationPredicates(predicates, root, criteriaBuilder, propertySearchDto);
            addNumericPredicates(predicates, root, criteriaBuilder, propertySearchDto);
            addBooleanPredicates(predicates, root, criteriaBuilder, propertySearchDto);
            addListPredicates(predicates, root, propertySearchDto);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addLocationPredicates(List<Predicate> predicates, Root<Property> root,
            CriteriaBuilder criteriaBuilder, PropertySearchDto dto) {

        if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(LOCATION).get("address")),
                    "%" + dto.getAddress().toLowerCase() + "%"));
        }

        if (dto.getCity() != null && !dto.getCity().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(LOCATION).get("city")),
                    dto.getCity().toLowerCase()));
        }

        if (dto.getState() != null && !dto.getState().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(LOCATION).get("state")),
                    dto.getState().toLowerCase()));
        }

        if (dto.getCountry() != null && !dto.getCountry().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(LOCATION).get("country")),
                    dto.getCountry().toLowerCase()));
        }

        if (dto.getPostalCode() != null && !dto.getPostalCode().trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get(LOCATION).get("postalCode"),
                    dto.getPostalCode()));
        }
    }

    private static void addNumericPredicates(List<Predicate> predicates, Root<Property> root,
            CriteriaBuilder cb, PropertySearchDto dto) {

        addRangePredicates(predicates, root, cb, "pricePerMonth", dto.getPricePerMonth());
        addRangePredicates(predicates, root, cb, "securityDeposit", dto.getSecurityDeposit());
        addRangePredicates(predicates, root, cb, "beds", dto.getBeds());
        addRangePredicates(predicates, root, cb, "baths", dto.getBaths());
        addRangePredicates(predicates, root, cb, "squareFeet", dto.getSquareFeet());
        addRangePredicates(predicates, root, cb, "averageRatings", dto.getAverageRatings());
        addRangePredicates(predicates, root, cb, "numberOfRatings", dto.getNumberOfRatings());
    }

    private static void addRangePredicates(List<Predicate> predicates, Root<Property> root,
            CriteriaBuilder cb, String fieldName, NumericRangesDto range) {
        if (range == null) {
            return;
        }

        if (range.getLt() != null) {
            predicates.add(cb.lessThan(root.get(fieldName), range.getLt()));
        }
        if (range.getLte() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), range.getLte()));
        }
        if (range.getGt() != null) {
            predicates.add(cb.greaterThan(root.get(fieldName), range.getGt()));
        }
        if (range.getGte() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), range.getGte()));
        }
        if (range.getEq() != null) {
            predicates.add(cb.equal(root.get(fieldName), range.getEq()));
        }
    }

    private static void addBooleanPredicates(List<Predicate> predicates, Root<Property> root,
            CriteriaBuilder criteriaBuilder, PropertySearchDto dto) {

        if (dto.isPetAllowed()) {
            predicates.add(criteriaBuilder.isTrue(root.get("petAllowed")));
        }

        if (dto.isParkingIncluded()) {
            predicates.add(criteriaBuilder.isTrue(root.get("parkingIncluded")));
        }
    }

    private static void addListPredicates(List<Predicate> predicates, Root<Property> root, PropertySearchDto dto) {

        if (dto.getAmenities() != null && !dto.getAmenities().isEmpty()) {
            predicates.add(root.get("amenities").in(dto.getAmenities()));
        }

        if (dto.getHighlights() != null && !dto.getHighlights().isEmpty()) {
            predicates.add(root.get("highlights").in(dto.getHighlights()));
        }
    }
}
