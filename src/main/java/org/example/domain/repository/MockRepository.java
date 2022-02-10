package org.example.domain.repository;

public interface MockRepository {
    default void trace() {
        System.out.printf("   *** [%s.%s] %n", this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName());
    }
}
