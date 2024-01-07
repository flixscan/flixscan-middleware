package com.flixscan.middleware.user;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@ApplicationScoped
public class UserService {

    public Uni<List<UserEntity>> getAllUser() {
        return UserEntity.listAll();
    }

    public Uni<UserEntity> findItemById(Long id) {
        return UserEntity.findById(id);
    }

    public Uni<List<UserEntity>> findAllByPage(int limit, int offset) {
        return UserEntity.findAll().page(limit, offset).list();
    }

    public Uni<Response> createUser(UserEntity user) {
        return Panache.withTransaction(() ->
                user.persist().replaceWith(() -> Response.ok(user).status(CREATED).build()));
    }

    public Uni<Response> updateUser(Long id, UserEntity updatedUser) {
        return Panache.withTransaction(() ->
                updatedUser.<UserEntity>findById(id)
                        .onItem().ifNotNull().transform(entity -> {
                            entity.setUserName(updatedUser.getUserName());
                            entity.setUserEmail(updatedUser.getUserEmail());
                            entity.setUserPass(updatedUser.getUserPass());
                            entity.setUserSalt(updatedUser.getUserSalt());
                            entity.setUserMobile(updatedUser.getUserMobile());
                            entity.setUserRoles(updatedUser.getUserRoles());
                            entity.setIsActive(updatedUser.getIsActive());
                            entity.setIsVerified(updatedUser.getIsVerified());
                            entity.setValidUntil(updatedUser.getValidUntil());
                            entity.setLastLogin(updatedUser.getLastLogin());
                            entity.setPasswordRequestedAt(updatedUser.getPasswordRequestedAt());
                            entity.setCreatedAt(updatedUser.getCreatedAt());
                            entity.setUpdatedAt(Instant.now());
                            entity.persist();
                            return Response.ok(entity).build();
                        }).onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build)
        );
    }
    public Uni<Response> deleteUser(Long id) {
        return Panache.withTransaction(() -> UserEntity.deleteById(id))
                .map(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }
}
