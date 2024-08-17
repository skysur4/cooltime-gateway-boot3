package io.cooltime.gateway.repository.user;

import org.springframework.stereotype.Repository;

import io.cooltime.gateway.entity.user.UserDetail;
import io.cooltime.gateway.repository.GatewayReactiveCrudRepository;


@Repository
public interface UserDetailRepository extends GatewayReactiveCrudRepository<UserDetail, String> {
}

