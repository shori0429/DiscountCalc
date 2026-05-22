# 一括割引計算アプリ(Android)

![Android](https://img.shields.io/badge/-Android-{#3DDC84}?logo=android)

Java / Android Studioで作成した割引計算アプリ

## 作成経緯
主にJavaでアプリ開発を行う学習の為

ユーザーが入力した金額に対して、複数の割引率で一括計算・比較できるアプリがあると便利だと思い作成


## 実装機能
- 割引率計算
- Roomライブラリ使用(バグ多数で未修正状態)
- Link to the sample section: [Link Text](#sample-section).
- Fragment画面遷移
- ListAdapter,DiffUtilを使用した一覧表示
- 自分で使用する割引率を設定・保存


## アプリ画面( 環境:エミュレータ[Pixel 9a:API37] )

### Presetモード
あらかじめ用意された割引率を使用する

<img width="256" height="575" alt="Screenshot_20260522_141937" src="https://github.com/user-attachments/assets/0dc74f56-5cac-4e90-9d38-017c398f8874" />

### Customモード

[カスタム設定画面]&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;[保存データ使用]

<p>
  <img width="256" height="575" alt="Screenshot_20260522_145346" src="https://github.com/user-attachments/assets/ff8af7a2-5159-4a36-98f6-9b032fd8bd96" />
  <img width="256" height="575" alt="Screenshot_20260522_145336" src="https://github.com/user-attachments/assets/c17cb51e-cc4b-43e9-a9f2-76d1b3e2b344" />
</p>


## 使用技術
- Java
- Android Studio
- MVVM
- LiveData
- Room
- RecyclerView
- DiffUtil


## 意識したもの
- Activityを1つだけにして、複数のFragmentで画面管理
- MVVMを意識してできるだけロジック分離
- ListAdapter,DiffUtilを利用してリスト表示を効率化


## 改善予定
- Material3を利用したアプリバーへの刷新
- 設定画面をスライドメニューへ統合
- リスト表示のレイアウト調整
- Customデータ未登録時の制御改善


## 既知のバグ(2026/05/22)
- Customデータの保存処理が上手くできていない
- Customデータ設定画面で割引率の入力後、そのテキストフィールドから一度フォーカスを外さないと、編集前の値が保存されてしまう
- Customデータ設定画面で全削除を押したときに偶にIndexOutOfBoundsExceptionが発生してしまう。ViewModelとAdapterの更新タイミングの問題
- 使用するデータがCustomになっている時、設定Topを開くと表示数スライダーが1になってしまう
