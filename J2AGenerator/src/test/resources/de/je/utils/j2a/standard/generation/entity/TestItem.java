/**
 *
 * {@link GameReleasePriceTO}.java
 *
 * @author Jens Ebert
 *
 * @date 12.07.2019
 *
 */
package de.je.utils.j2a.standard.generation.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * {@link TestItem} represents price data for a game release. There can be
 * multiple entries (historical prices or different sources) for a game release.
 */
@Entity
@Table(name = "test_item")
public class TestItem {

	@Id
	private Long id;

	@Version
	private Long version;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "price_euro_cents")
	private Integer priceEuroCents;

	@Column(name = "platform_id")
	private Long platformId;

	@Column(name = "game_release_id")
	private Long gameReleaseId;

	@Column(name = "test_item_id")
	private String testItemId;

	@Column(name = "test_view_item_link")
	private String testViewItemLink;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	public LocalDate getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Long getGameReleaseId() {
		return gameReleaseId;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public Integer getPriceEuroCents() {
		return priceEuroCents;
	}

	public String getTestItemId() {
		return testItemId;
	}

	public String getTestViewItemLink() {
		return testViewItemLink;
	}

	public String getTitle() {
		return title;
	}

	public String getTotalItemText() {
		return getTitle() + (getDescription() != null ? getDescription() : "");
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGameReleaseId(Long gameReleaseId) {
		this.gameReleaseId = gameReleaseId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public void setPriceEuroCents(Integer priceEuroCents) {
		this.priceEuroCents = priceEuroCents;
	}

	public void setTestItemId(String ebayItemId) {
		testItemId = ebayItemId;
	}

	public void setTestViewItemLink(String ebayViewItemLink) {
		testViewItemLink = ebayViewItemLink;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}