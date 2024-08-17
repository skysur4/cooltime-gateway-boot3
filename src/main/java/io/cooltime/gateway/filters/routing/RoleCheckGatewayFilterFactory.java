package io.cooltime.gateway.filters.routing;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class RoleCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleCheckGatewayFilterFactory.Config> {

	public GatewayFilter apply() {
		Config c = new Config();
		c.reset();
		
		return apply(c);
	}
	
    @Override
    public GatewayFilter apply(Config config) {
    	
    	return (exchange, chain) -> {
    		return chain.filter(exchange);
    	};
    }

	public static class Config {
		private String roleId;
		private String roleName;
		
		public void reset() {
			setRoleId(null);
			setRoleName(null);
		}
		public boolean hasRole() {
			return StringUtils.isNotEmpty(roleId);
		}
		public String getRoleId() {
			return roleId;
		}
		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
	}
}
