package com.infos.androidtask.data.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "taskModel")
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id :Int,
    @SerializedName("BusinessUnitKey")
    @ColumnInfo val businessUnitKey: String?,
    @SerializedName("businessUnit")
    @ColumnInfo val businessUnit: String?,
    @SerializedName("colorCode")
    @ColumnInfo val colorCode: String?,
    @SerializedName("description")
    @ColumnInfo val description: String?,
    @SerializedName("isAvailableInTimeTrackingKioskMode")
    @ColumnInfo val isAvailableInTimeTrackingKioskMode: Boolean,
    @SerializedName("parentTaskID")
    @ColumnInfo val parentTaskId: String?,
    @SerializedName("sort")
    @ColumnInfo val sort: String?,
    @SerializedName("task")
    @ColumnInfo val task: String?,
    @SerializedName("title")
    @ColumnInfo val title: String?,
    @SerializedName("wageType")
    @ColumnInfo val wageType: String?,
)
