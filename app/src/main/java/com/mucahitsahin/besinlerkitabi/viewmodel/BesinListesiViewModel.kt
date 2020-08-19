package com.mucahitsahin.besinlerkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mucahitsahin.besinlerkitabi.model.Besin
import com.mucahitsahin.besinlerkitabi.servis.BesinAPIServis
import com.mucahitsahin.besinlerkitabi.servis.BesinDatabase
import com.mucahitsahin.besinlerkitabi.util.OzelSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application):BaseViewModel(application) {
    val besinler=MutableLiveData<List<Besin>>()
    val besinHataMesaji=MutableLiveData<Boolean>()
    val besinYukleniyor=MutableLiveData<Boolean>()
    private var guncellemeZamani=10*60*1000*1000*1000L

    private val besinApiServis=BesinAPIServis()
    private val disposable=CompositeDisposable()
    private  val ozelSharedPreferences=OzelSharedPreferences(getApplication())

    fun refreshData(){
        val kaydedilmeZamani=ozelSharedPreferences.zamaniAl()
        if(kaydedilmeZamani!=null&&kaydedilmeZamani!=0L&&System.nanoTime()-kaydedilmeZamani<guncellemeZamani){
            //sqlitedan çek
            verileriSQLitetanAl()
        }
        else{
            verileriInternettenAl()
        }
    }

    fun refreshFromInternet(){
        verileriInternettenAl()
    }

    private fun verileriSQLitetanAl(){

        launch {

            val besinListesi= BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Besinleri Roomdan Aldık",Toast.LENGTH_LONG).show()
        }

    }

    private fun verileriInternettenAl(){
        besinYukleniyor.value=true

        disposable.add(
            besinApiServis.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Besin>>(){
                    override fun onSuccess(t: List<Besin>) {
                        //islem basarili olursa
                        sqliteSakla(t)
                        Toast.makeText(getApplication(),"Besinleri İnternetten Aldık",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        //hata alırsak
                        besinHataMesaji.value=true
                        besinYukleniyor.value=false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun besinleriGoster(besinlerListesi:List<Besin>){
        besinler.value=besinlerListesi
        besinHataMesaji.value=false
        besinYukleniyor.value=false
    }

    private fun sqliteSakla(besinListesi:List<Besin>){

        launch {

            val dao=BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi= dao.insertAll(*besinListesi.toTypedArray())
            var i=0
            while (i<besinListesi.size){
                besinListesi[i].uuid=uuidListesi[i].toInt()
            }
            besinleriGoster(besinListesi)
        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime() )

    }

}