/**
 * 
 */
package com.hst.ii.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.hst.core.annotation.Comment;
import com.hst.core.annotation.HSID;
import com.hst.core.dao.BaseEntity;
import com.hst.core.meta.annotation.EField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luwenjie
 *
 */
@Entity
@Table(name = "sys_user", indexes = { @Index(unique = true, columnList = "userid")})
@HSID(length = 10, prefix = "U", resetByDay = true)
@Comment("系统用户表")
@Data
@NoArgsConstructor
public class TUser extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 10)
	@GeneratedValue(generator = "hsid")
	@Comment("ID")
	private String id;

	@Column(length = 30)
	@Comment("用户id")
	private String userid;

	@Column(length = 20)
	@Comment("姓名")
	private String name;

	@Column(length = 20)
	@Comment("密码")
	private String passwd;

	@Column(length = 50)
	@Comment("Email")
	private String email;

	@Column(length = 15)
	@Comment("mobile")
	private String mobile;

	@Column(name="id_org",length = 10)
	@Comment("所属机构")
	private String orgId;

	@Column(length = 32)
	@Comment("openid")
	private String openid;

	@Column(length = 2)
	@Comment("状态")
	private String status;

	@Column(length = 1)
	@Comment("锁定(0:未锁定状态/ 1：锁定状态)")
	private String locked;

}