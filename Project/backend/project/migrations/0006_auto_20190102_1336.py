# Generated by Django 2.1.4 on 2019-01-02 13:36

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('project', '0005_project_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='project',
            name='image',
            field=models.ImageField(default='images/projects/no-image.jpg', upload_to='images/projects'),
        ),
    ]
