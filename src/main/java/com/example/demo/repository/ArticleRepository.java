package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Article;

/**
 * articlesテーブルの情報を操作するリポジトリ.
 * 
 * @author masashi.nose
 *
 */
@Repository
public class ArticleRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final String TABLE_NAME = "articles";

	public static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
		Article article = new Article();
		article.setId(rs.getInt("id"));
		article.setName(rs.getString("name"));
		article.setContent(rs.getString("content"));
		return article;
	};

	/**
	 * 投稿記事を全件検索する.
	 * 
	 * @return 記事全件データ
	 */
	public List<Article> findAll() {
		String sql = "select id, name, content from " + TABLE_NAME + " order by id desc";

		List<Article> articleList = template.query(sql, ARTICLE_ROW_MAPPER);

		return articleList;
	}

	/**
	 * 記事を投稿する.
	 * 
	 * @param article リクエストパラメータ
	 */
	public void insert(Article article) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);

		String insertSql = "insert into " + TABLE_NAME + " (name, content) values (:name, :content)";

		template.update(insertSql, param);
	}

	/**
	 * 記事を削除する.
	 * 
	 * @param id リクエストパラメータ
	 */
	public void deleteById(Integer id) {
		String deleteSql = "delete from " + TABLE_NAME + " where id = :id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		template.update(deleteSql, param);

	}

}
