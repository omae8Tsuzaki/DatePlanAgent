package com.dateplan.entity;

/**
 * <p>レストランの情報を表すクラス。</p>
 *
 * @param name レストランの名前
 * @param address レストランの住所
 * @param genre レストランのジャンル（例: "イタリアン", "和食"など）
 * @param budget レストランの予算（例: "1000-2000円"）
 * @param access レストランへのアクセス情報（例: "最寄り駅から徒歩5分"）
 * @param url レストランのURL
 * @param photo レストランの写真URL
 */
public record Restaurant(String name, String address, String genre, String budget, String access, String url,
                          String photo) {
}
