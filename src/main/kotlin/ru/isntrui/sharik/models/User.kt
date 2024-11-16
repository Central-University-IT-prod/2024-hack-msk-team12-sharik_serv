package ru.isntrui.sharik.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username"])])
// @EqualsAndHashCode(exclude = ["user", "pays", "owes", "transfersPaid", "transfersReceived"])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID of the User", example = "597d634a-940d-48da-8b87-a2e68bc92cb1")
    var id: UUID = UUID.randomUUID(),
    @Schema(description = "Username of the User", example = "alloetosashabely")
    private var username: String = "",
    @Schema(description = "Password hash of the User", example = "123456")
    private var password: String = "",
    @Schema(description = "First name of the User", example = "Sasha")
    var firstName: String = "",
    @Schema(description = "Last name of the User", example = "Bely")
    var lastName: String = "",

    @Schema(description = "Avatar URL")
    var avatarURL: String = "",

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    @JsonIgnore
    var user: MutableSet<Debt> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "pays")
    @JsonIgnore
    var pays: MutableSet<Activity> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "owes")
    @JsonIgnore
    var owes: MutableSet<ActivityOwes> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "from")
    @JsonIgnore
    var transfersPaid: MutableSet<Transfer> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "to")
    @JsonIgnore
    var transfersReceived: MutableSet<Transfer> = mutableSetOf(),
) : UserDetails {
    constructor() : this(
        UUID.randomUUID(),
        "",
        "",
        "",
        "",
        "",
        mutableSetOf(),
        mutableSetOf(),
        mutableSetOf(),
        mutableSetOf(),
        mutableSetOf()
    )

    @JsonIgnore
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_USER"))

    @JsonIgnore
    override fun getPassword() = password

    override fun getUsername() = username

    @JsonIgnore
    override fun isAccountNonExpired() = true

    @JsonIgnore
    override fun isAccountNonLocked() = true

    @JsonIgnore
    override fun isCredentialsNonExpired() = true

    @JsonIgnore
    override fun isEnabled() = true

}
