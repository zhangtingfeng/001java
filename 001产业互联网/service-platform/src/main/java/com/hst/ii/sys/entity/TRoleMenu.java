package com.hst.ii.sys.entity;

import com.hst.core.annotation.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * @author Adrian
 *
 */
@Entity
@Table(name="sys_role_menu")
@Comment("角色菜单关系表")
@Data
@NoArgsConstructor
public class TRoleMenu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Comment("角色id")
	@Id
	@Column(length=10, name="id_role")
	private String roleId;
	
	@Comment("菜单id")
	@Id
	@Column(length=10, name="id_menu")
	private String menuId;

}
