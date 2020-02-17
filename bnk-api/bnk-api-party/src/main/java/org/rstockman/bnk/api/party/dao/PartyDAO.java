package org.rstockman.bnk.api.party.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rstockman.bnk.api.party.dto.PartyRequestParams;
import org.rstockman.bnk.api.party.dto.PartyResource;
import org.rstockman.bnk.common.dao.SimpleDAO;
import org.rstockman.bnk.common.service.KeyProvisionerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PartyDAO implements SimpleDAO<PartyResource, PartyRequestParams, String, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartyDAO.class);

	private static final List<String> DEFAULT_COLUMNS = Arrays.asList("_key", "_version", "_created", "_updated", "id",
			"name");

	@Autowired
	private KeyProvisionerService keyProvisioner;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Optional<PartyResource> get(String key) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(
					"select " + String.join(",", DEFAULT_COLUMNS) + " from party where _key = ?", (rs, rowNum) -> {
						return new PartyResource(rs.getLong("id"), rs.getString("name"), rs.getString("_key"),
								rs.getString("_version"), rs.getTimestamp("_created"), rs.getTimestamp("_updated"));

					}, key));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.warn("There should be 1 or 0 rows with this _key=" + key + " but more records were found.", e);
			return Optional.empty();
		}
	}

	@Override
	public List<PartyResource> getAll(PartyRequestParams params) {
		var columns = resolveColumns(params);
		var sorts = resolveSorts(params);
		var pagination = resolvePagination(params);
		var query = "select " + (String.join(",", columns)) + " from party" + sorts + pagination;
		LOGGER.debug(query);
		return jdbcTemplate.query(query, (rs, rowNum) -> {
			var obj = new PartyResource();
			if (columns.contains("_key")) {
				obj.setKey(rs.getString("_key"));
			}
			if (columns.contains("_version")) {
				obj.setVersion(rs.getString("_version"));
			}
			if (columns.contains("_created")) {
				obj.setCreated(rs.getTimestamp("_created"));
			}
			if (columns.contains("_updated")) {
				obj.setUpdated(rs.getTimestamp("_updated"));
			}
			if (columns.contains("id")) {
				obj.setId(rs.getLong("id"));
			}
			if (columns.contains("name")) {
				obj.setName(rs.getString("name"));
			}
			return obj;
		});
	}

	private List<String> resolveColumns(PartyRequestParams params) {
		if (Objects.isNull(params.getFields())) {
			return DEFAULT_COLUMNS;
		} else {
			return Arrays.asList(params.getFields().trim().split("\\s*,\\s*"));
		}
	}

	private String resolveSorts(PartyRequestParams params) {
		if (Objects.isNull(params.getSort())) {
			return " ";
		} else {
			return " ORDER BY "
					+ String.join(",", Arrays.asList(params.getSort().trim().split("\\s*,\\s*")).stream().map(s -> {
						if (s.startsWith("-")) {
							return s.substring(1) + " DESC ";
						} else if (s.startsWith("+")) {
							return s.substring(1) + " ASC ";
						} else {
							return s;
						}
					}).collect(Collectors.toList()));
		}
	}

	private String resolvePagination(PartyRequestParams params) {
		if (Objects.isNull(params.getPage()) && Objects.isNull(params.getLimit())) {
			return " ";
		} else {
			return " LIMIT " + (Objects.isNull(params.getPage()) ? 0 : params.getPage()) + ","
					+ (Objects.isNull(params.getLimit()) ? Integer.MAX_VALUE : params.getLimit()) + " ";
		}
	}

	@Override
	public String create(PartyResource obj) {
		var key = keyProvisioner.getKey();
		jdbcTemplate.update("insert into party (_key, id, name) values (?,?,?)", key, obj.getId(), obj.getName());
		return key;
	}

	@Override
	public void put(String key, PartyResource obj) {
		jdbcTemplate.update("update party set id = ?, name = ? where _key = ?", obj.getId(), obj.getName(), key);
	}

	@Override
	public void put(String key, String version, PartyResource obj) {
		jdbcTemplate.update("update party set id = ?, name = ?, _version = ? where _key = ?", obj.getId(),
				obj.getName(), key, version);
	}

	@Override
	public void delete(String key) {
		jdbcTemplate.update("delete from party where _key = ?", key);
	}

}
